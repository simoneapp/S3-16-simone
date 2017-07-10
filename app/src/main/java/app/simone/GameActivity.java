package app.simone;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.google.android.gms.games.Games;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import org.json.JSONException;
import PubNub.Request;
import PubNub.PubnubController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import akka.actor.ActorRef;
import app.simone.styleable.SimoneTextView;
import app.simone.users.model.FacebookUser;
import application.App;
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
import utils.*;
import utils.google.AchievementHelper;
import utils.google.LeaderboardCallback;


/**
 * @author Michele Sapignoli
 */

public class GameActivity extends FullscreenActivity implements IGameActivity {
    private boolean playerBlinking;
    private boolean tapToBegin = true;

    private FloatingActionButton gameFab;
    private PubnubController pnController;
    private boolean isMultiplayerMode = false;
    private SimoneTextView simoneTextView;

    private static final int INIT_SEED = 21;
    private boolean viewPaused;

    private int chosenMode = Constants.CLASSIC_MODE;

    private List<Button> buttons;
    private Integer[] shuffle = new Integer[]{0, 1, 2, 3};

    private FrameLayout[] layouts = new FrameLayout[4];

    int pippo=0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.CPU_TURN:
                    int score = msg.arg2 + 1;

                    if (playerBlinking) {
                        simoneTextView.setText(/*Score*/String.valueOf(score));
                        simoneTextView.startAnimation(AnimationHandler.getGameButtonAnimation());
                        if(App.getGoogleApiHelper().getGoogleApiClient().isConnected()){
                            Games.setViewForPopups(App.getGoogleApiHelper().getGoogleApiClient(), mContentView);
                            AchievementHelper.checkAchievement(score, chosenMode );
                        }
                    }
                    playerBlinking = false;
                    if(/*arg1 = 0 when PLAYER_TURN_MSG comes from GameViewActor*/msg.arg1!=0){
                        blinkDelayed(msg);
                    }
                    break;
                case Constants.PLAYER_TURN:
                    if (!playerBlinking) {
                        simoneTextView.setText(Constants.TURN_PLAYER);
                        simoneTextView.startAnimation(AnimationHandler.getGameButtonAnimation());
                        if (chosenMode == Constants.HARD_MODE) {
                            swapButtonPositions();
                        }
                    }
                    playerBlinking = true;
                    if(/*arg1 = 0 when PLAYER_TURN_MSG comes from GameViewActor*/msg.arg1!=0){
                        blinkDelayed(msg);
                    }
                    break;
                case Constants.WHATTASHAMEYOULOST_MSG:
                    if(App.getGoogleApiHelper().isConnected()){
                        Games.Leaderboards.submitScoreImmediate(App.getGoogleApiHelper().getGoogleApiClient(), Constants.LEADERBOARD_ID, msg.arg1)
                        .setResultCallback(new LeaderboardCallback());
                    } else {
                        //TODO WRITE PENDING SCORE SU DB
                    }

                    tapToBegin = true;
                    simoneTextView.setText(Constants.PLAY_AGAIN);
                    simoneTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                    gameFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#990000")));
                    simoneTextView.startAnimation(AnimationHandler.getGameButtonAnimation());
                    break;
            }

        }
    };

    private void blinkDelayed(Message msg){
            // Message from ViewActor or this activity itself, handling the blinking
            Button b = (Button) findViewById(msg.arg1);
            b.setAlpha(0.4f);
            new AudioPlayer().play(getApplicationContext(), SColor.fromInt(msg.arg1).getSoundId());
            Message m = new Message();
            m.what = msg.what;
            m.arg1 = msg.arg1;
            vHandler.sendMessageDelayed(m, Constants.STD_DELAY_BTN_TIME);

    }

    private Handler vHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Button b = (Button) findViewById(msg.arg1);
            b.setAlpha(1);

            switch (msg.what) {
                case Constants.CPU_TURN:
                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().getActorSystem())
                            .tell(new NextColorMsg(), ActorRef.noSender());
                    break;
                case Constants.PLAYER_TURN:
                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().getActorSystem())
                            .tell(new GuessColorMsg(SColor.fromInt(msg.arg1)), ActorRef.noSender());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chosenMode = getIntent().getIntExtra(Constants.CHOSEN_MODE, Constants.CLASSIC_MODE);


        if (getIntent().getExtras().get("player") != null) {
            isMultiplayerMode = true;
            pnController = new PubnubController("multiplayer");
            pnController.subscribeToChannel();

            FacebookUser player = (FacebookUser) getIntent().getExtras().getSerializable("player");
            FacebookUser toPlayer = (FacebookUser) getIntent().getExtras().getSerializable("toPlayer");
            Request req = new Request(player,toPlayer);
            try {
                pnController.publishToChannel(req);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("GameActivity", "Error while publishing the message on the channel");
            }
        }

        buttons = new ArrayList<>();

        gameFab = (FloatingActionButton) findViewById(R.id.game_fab);
        simoneTextView = (SimoneTextView) findViewById(R.id.game_simone_textview);

        AnimationHandler.initAnimations(this, gameFab, simoneTextView);

        layouts[0] = (FrameLayout) findViewById(R.id.game_top_left_frame);
        layouts[1] = (FrameLayout) findViewById(R.id.game_top_right_frame);
        layouts[2] = (FrameLayout) findViewById(R.id.game_bottom_left_frame);
        layouts[3] = (FrameLayout) findViewById(R.id.game_bottom_right_frame);

        gameFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AudioManager.Companion.getInstance().stopSimoneMusic();

                if (tapToBegin) {
                    tapToBegin = false;
                    playerBlinking = false;
                    simoneTextView.startAnimation(AnimationHandler.getGameButtonAnimation());
                    simoneTextView.setText(Constants.STRING_EMPTY);
                    simoneTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#737373")));
                    gameFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f2f2f2")));

                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.CPU_ACTOR_NAME, App.getInstance().getActorSystem())
                            .tell(new StartGameVsCPUMsg(), ActorRef.noSender());
                }
            }
        });

        Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().getActorSystem())
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

    public boolean isPlayerBlinking() {
        return this.playerBlinking;
    }

    public void setPlayerBlinking(boolean isPlayerTurn) {
        this.playerBlinking = isPlayerTurn;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!playerBlinking) {
            this.viewPaused = true;
            Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().getActorSystem())
                    .tell(new PauseMsg(true), ActorRef.noSender());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!playerBlinking && viewPaused) {
            this.viewPaused = false;
            Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().getActorSystem())
                    .tell(new PauseMsg(false), ActorRef.noSender());
        }
    }

    @Override
    protected void backTransition() {
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    private void swapButtonPositions() {
        Collections.shuffle(Arrays.asList(shuffle));

        for (FrameLayout f : layouts) {
            f.removeAllViews();
        }
        AnimationHandler.performColorSwapAnimation(this, this.shuffle, this.buttons, this.layouts);
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


    private Button initColorButton(final SColor color) {
        final Button button = (Button) findViewById(color.getButtonId());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerBlinking && !tapToBegin) {
                    Message m = new Message();
                    m.arg1 = color.getButtonId();
                    m.what = Constants.PLAYER_TURN;
                    handler.sendMessage(m);
                }
            }
        });
        return button;
    }

    private void addScoreListener() {
        pnController.getPubnub().addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                if (message.getMessage() != null) {
                    final PNMessageResult msg = message;
                    System.out.println(message.getMessage().toString());

                    printScore(GameActivity.this, message);


                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });
    }

    private void printScore(final Activity parent, PNMessageResult message) {
        if (message.getMessage() != null) {
            final PNMessageResult msg = message;
            System.out.println(message.getMessage().toString());

            parent.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(parent.getBaseContext(), "Your score is: " + msg.getMessage().toString(), Toast.LENGTH_LONG).show();
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
