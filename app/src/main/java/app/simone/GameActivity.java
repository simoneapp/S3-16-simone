package app.simone;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import PubNub.PubnubController;
import PubNub.Request;
import akka.actor.ActorRef;
import app.simone.styleable.SimoneTextView;
import app.simone.users.model.FacebookUser;
import application.mApplication;
import colors.SColor;
import messages.AttachViewMsg;
import messages.GuessColorMsg;
import messages.NextColorMsg;
import messages.PauseMsg;
import messages.StartGameVsCPUMsg;
import utils.AudioManager;
import utils.AudioPlayer;
import utils.Constants;
import utils.Utilities;


/**
 * @author Michele Sapignoli
 */

public class GameActivity extends FullscreenActivity implements IGameActivity {
    private boolean playerTurn;
    private boolean tapToBegin = true;

    private Animation animation;

    private FloatingActionButton gameFab;
    private PubnubController pnController;
    private boolean isMultiplayerMode=false;
    private SimoneTextView simoneTextView;

    private static final int INIT_SEED = 21;
    private boolean paused;

    private int chosenMode = Constants.CLASSIC_MODE;

    private List<Button> buttons;
    private Integer[] shuffle = new Integer[]{0, 1, 2, 3};

    private FrameLayout[] layouts = new FrameLayout[4];

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.CPU_TURN:
                    if (playerTurn) {

                        simoneTextView.setText(/*Score*/String.valueOf(msg.arg2 + 1));
                        simoneTextView.startAnimation(animation);
                    }
                    playerTurn = false;

                    break;
                case Constants.PLAYER_TURN:
                    if (!playerTurn) {
                        simoneTextView.setText(Constants.TURN_PLAYER);
                        simoneTextView.startAnimation(animation);

                        if (chosenMode == Constants.HARD_MODE) {
                            swapButtonPositions();
                        }
                    }
                    playerTurn = true;

                    break;
                case Constants.WHATTASHAMEYOULOST_MSG:
                    tapToBegin = true;
                    simoneTextView.setText(Constants.PLAY_AGAIN);
                    simoneTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                    gameFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#990000")));
                    simoneTextView.startAnimation(animation);
                    break;
            }

            if (msg.arg1 != 0) {
                // Message from ViewActor or this activity itself, handling the blinking
                Button b = (Button) findViewById(msg.arg1);
                b.setAlpha(0.4f);
                SColor color = SColor.fromInt(msg.arg1);
                AudioPlayer player = new AudioPlayer();
                player.play(getApplicationContext(), color.getSoundId());
                Message m = new Message();
                m.what = msg.what;
                m.arg1 = msg.arg1;
                vHandler.sendMessageDelayed(m, 300);
            }


        }
    };


    private Handler vHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Button b = (Button) findViewById(msg.arg1);
            b.setAlpha(1);

            switch (msg.what) {
                case Constants.CPU_TURN:
                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, mApplication.getActorSystem())
                            .tell(new NextColorMsg(), ActorRef.noSender());
                    break;
                case Constants.PLAYER_TURN:
                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, mApplication.getActorSystem())
                            .tell(new GuessColorMsg(SColor.fromInt(msg.arg1)), ActorRef.noSender());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras().get("player")!=null){
            isMultiplayerMode=true;
            pnController = new PubnubController("multiplayer");
            pnController.subscribeToChannel();

            FacebookUser player = (FacebookUser) getIntent().getExtras().getSerializable("player");
            FacebookUser toPlayer = (FacebookUser) getIntent().getExtras().getSerializable("toPlayer");
            Request req = new Request(player,toPlayer);
            try {
                pnController.publishToChannel(req);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("GameActivity","Error while publishing the message on the channel");
            }
        }

        int radiobtnIndex = 0;
        buttons = new ArrayList<>();

        Intent intent = getIntent();
        chosenMode = intent.getIntExtra(Constants.CHOSEN_MODE, Constants.CLASSIC_MODE);

        initAnimation();

        layouts[0] = (FrameLayout) findViewById(R.id.game_top_left_frame);
        layouts[1] = (FrameLayout) findViewById(R.id.game_top_right_frame);
        layouts[2] = (FrameLayout) findViewById(R.id.game_bottom_left_frame);
        layouts[3] = (FrameLayout) findViewById(R.id.game_bottom_right_frame);

        gameFab = (FloatingActionButton) findViewById(R.id.game_fab);
        simoneTextView = (SimoneTextView) findViewById(R.id.game_simone_textview);
        gameFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AudioManager.Companion.getInstance().stopSimoneMusic();

                if (tapToBegin) {
                    tapToBegin = false;
                    playerTurn = false;
                    simoneTextView.startAnimation(animation);
                    simoneTextView.setText(Constants.STRING_EMPTY);
                    simoneTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#737373")));
                    gameFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f2f2f2")));

                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.CPU_ACTOR_NAME, mApplication.getActorSystem())
                            .tell(new StartGameVsCPUMsg(), ActorRef.noSender());
                }
            }
        });

        Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, mApplication.getActorSystem())
                .tell(new AttachViewMsg(this), ActorRef.noSender());
    }

    @Override
    protected void setSubclassContentView() {
        setContentView(R.layout.activity_game);
        mContentView = findViewById(R.id.game_fullscreen_content);
    }


    public Handler getHandler() {
        return this.handler;
    }

    public void setPlayerTurn(boolean isPlayerTurn) {
        this.playerTurn = isPlayerTurn;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!playerTurn) {
            this.paused = true;
            Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, mApplication.getActorSystem())
                    .tell(new PauseMsg(true), ActorRef.noSender());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!playerTurn && paused) {
            this.paused = false;
            Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, mApplication.getActorSystem())
                    .tell(new PauseMsg(false), ActorRef.noSender());
        }
    }

    @Override
    protected void backTransition() {
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    protected void forwardTransition() {
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private void swapButtonPositions() {
        Collections.shuffle(Arrays.asList(shuffle));

        for (FrameLayout f : layouts) {
            f.removeAllViews();
        }
        for (int i = 0; i < this.buttons.size(); i++) {
            int index = shuffle[i];
            layouts[i].addView(buttons.get(index));

            ObjectAnimator objectAnimator = ObjectAnimator.ofObject(buttons.get(i), "backgroundColor",
                    new ArgbEvaluator(),
                    ContextCompat.getColor(this, SColor.getColorIdFromButtonId(buttons.get(index).getId())),
                    ContextCompat.getColor(this, SColor.getColorIdFromButtonId(buttons.get(i).getId())));

// 2
            objectAnimator.setRepeatCount(0);
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);

// 3
            objectAnimator.setDuration(300);
            objectAnimator.start();

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (buttons.size() == 0) {
            buttons.add(initColorButton(SColor.GREEN));
            buttons.add(initColorButton(SColor.RED));
            buttons.add(initColorButton(SColor.YELLOW));
            buttons.add(initColorButton(SColor.BLUE));
        }
    }

    private void initAnimation(){
        final Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        final Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        final Animation zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        this.animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        this.animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                if (playerTurn) {
                    gameFab.startAnimation(zoomIn);
                    simoneTextView.setAnimation(zoomIn);
                } else {
                    simoneTextView.startAnimation(zoomOut);
                    gameFab.startAnimation(zoomOut);
                }
                simoneTextView.startAnimation(rotate);
            }
            @Override
            public void onAnimationEnd(Animation animation) { }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    private Button initColorButton(final SColor color) {
        final Button button = (Button) findViewById(color.getButtonId());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerTurn && !tapToBegin) {
                    Message m = new Message();
                    m.arg1 = color.getButtonId();
                    m.what = Constants.PLAYER_TURN;
                    handler.sendMessage(m);
                }
            }
        });
        return button;
    }

    private void addScoreListener(){
        pnController.getPubnub().addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                if (message.getMessage() != null) {
                    final PNMessageResult msg = message;
                    System.out.println(message.getMessage().toString());

                    printScore(GameActivity.this,message);


                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });
    }

    private void printScore(final Activity parent, PNMessageResult message){
        if (message.getMessage() != null) {
            final PNMessageResult msg = message;
            System.out.println(message.getMessage().toString());

            parent.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(parent.getBaseContext(), "Your score is: "+msg.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AudioManager.Companion.getInstance().playSimoneMusic();
    }
}
