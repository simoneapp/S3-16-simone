package app.simone;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

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

}
