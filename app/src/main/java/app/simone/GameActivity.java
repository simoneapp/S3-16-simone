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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import org.json.JSONException;
import PubNub.OnlinePlayer;
import PubNub.Request;
import java.util.Random;
import PubNub.PubnubController;
import akka.actor.ActorRef;
import app.simone.styleable.SimoneTextView;
import application.mApplication;
import colors.SColor;
import messages.AttachViewMsg;
import messages.GuessColorMsg;
import messages.NextColorMsg;
import messages.StartGameVsCPUMsg;
import utils.Constants;
import utils.Utilities;


/**
 * @author Michele Sapignoli
 */

public class GameActivity extends FullscreenActivity implements IGameActivity {
    private boolean playerTurn;
    private boolean tapToBegin = true;
    private SimoneTextView simoneTextView;
    private Animation animation;
    private FloatingActionButton gameFab;
    private PubnubController pnController;
    private boolean isMultiplayerMode=false;

    private static final int INIT_SEED = 21;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.CPU_TURN:
                    if(playerTurn){
                        simoneTextView.setText(Constants.STRING_EMPTY);
                        simoneTextView.startAnimation(animation);
                    }
                    playerTurn = false;

                    break;
                case Constants.PLAYER_TURN:
                    if(!playerTurn){
                        simoneTextView.setText(Constants.TURN_PLAYER);
                        simoneTextView.startAnimation(animation);
                    }
                    playerTurn = true;

                    break;
                case Constants.WHATTASHAMEYOULOST_MSG:
                    tapToBegin = true;
                    simoneTextView.setText(Constants.PLAY_AGAIN);
                    simoneTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                    gameFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#990000")));
                    simoneTextView.startAnimation(animation);
                    if(isMultiplayerMode){
                        addScoreListener();
                        String score = Integer.toString(getScore(INIT_SEED));

                        //JSONObject data = new JSONObject();

                      /*  try {
                            data.put("score", Integer.toString(getScore(INIT_SEED)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pnController.publishToChannel(data);*/
                      //pnController.publishToChannel(score);
                    }
                    break;
            }

            if(msg.arg1 != 0){
                // Message from ViewActor or this activity itself, handling the blinking
                Button b = (Button) findViewById(msg.arg1);
                b.setAlpha(0.4f);
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

            OnlinePlayer player = (OnlinePlayer) getIntent().getExtras().getSerializable("player");
            OnlinePlayer toPlayer = (OnlinePlayer) getIntent().getExtras().getSerializable("toPlayer");
            Request req = new Request(player,toPlayer);
            try {
                pnController.publishToChannel(req);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("GameActivity","Error while publishing the message on the channel");
            }
        }

        int radiobtnIndex = 0;

        if (savedInstanceState != null) {
            radiobtnIndex = savedInstanceState.getInt(Constants.RADIOBTN_INDEX_KEY);
        }

        initColorButton(SColor.GREEN);
        initColorButton(SColor.RED);
        initColorButton(SColor.YELLOW);
        initColorButton(SColor.BLUE);

        this.animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        gameFab = (FloatingActionButton) findViewById(R.id.game_fab);
        simoneTextView = (SimoneTextView) findViewById(R.id.game_simone_textview);
        gameFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tapToBegin) {
                    tapToBegin = false;
                    simoneTextView.setText(Constants.STRING_EMPTY);
                    simoneTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#737373")));
                    gameFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f2f2f2")));

                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.CPU_ACTOR_NAME, mApplication.getActorSystem())
                            .tell(new StartGameVsCPUMsg(), ActorRef.noSender());
                }
            }
        });


        /*
        Pass the instance of the GameActivity to GameViewActor
         */

        Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, mApplication.getActorSystem())
                .tell(new AttachViewMsg(this, radiobtnIndex), ActorRef.noSender());
    }

    private int getScore(int n){
        return new Random().nextInt(n);
    }

    @Override
    protected void setSubclassContentView() {
        setContentView(R.layout.activity_game);
        mContentView = findViewById(R.id.game_fullscreen_content);
    }

    private boolean initColorButton(final SColor color) {
        Button button = (Button) findViewById(color.getValue());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerTurn) {
                    Message m = new Message();
                    m.arg1 = color.getValue();
                    m.what = Constants.PLAYER_TURN;
                    handler.sendMessage(m);
                }
            }
        });
        return true;
    }

    public Handler getHandler() {
        return this.handler;
    }

    public void setPlayerTurn(boolean isPlayerTurn) {
        this.playerTurn = isPlayerTurn;
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

}
