package app.simone;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import akka.actor.ActorRef;
import application.mApplication;
import messages.AttachViewMsg;
import messages.GuessColorMsg;
import utils.Constants;
import utils.Utilities;

/**
 * @author Michele Sapignoli
 */

public class GameActivity extends FullscreenActivity implements IGameActivity {
    private Button greenButton;
    private Button redButton;
    private Button blueButton;
    private Button yellowButton;
    private boolean playerTurn;

    private Handler actorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    greenButton.setAlpha(0.4f);
                    actorHandler.sendEmptyMessageDelayed(4, 500);
                    break;
                case 1:
                    redButton.setAlpha(0.4f);
                    actorHandler.sendEmptyMessageDelayed(4, 500);
                    break;
                case 2:
                    blueButton.setAlpha(0.4f);
                    actorHandler.sendEmptyMessageDelayed(4, 500);
                    break;
                case 3:
                    yellowButton.setAlpha(0.4f);
                    actorHandler.sendEmptyMessageDelayed(4, 500);
                    break;
                case 4:
                    redButton.setAlpha(1);
                    blueButton.setAlpha(1);
                    yellowButton.setAlpha(1);
                    greenButton.setAlpha(1);
                    break;

            }

        }
    };

    private Handler playerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    greenButton.setAlpha(0.4f);
                    playerHandler.sendEmptyMessageDelayed(4, 500);
                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, mApplication.getActorSystem())
                            .tell(new GuessColorMsg(0), ActorRef.noSender());
                    break;
                case 1:
                    redButton.setAlpha(0.4f);
                    playerHandler.sendEmptyMessageDelayed(4, 500);
                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, mApplication.getActorSystem())
                            .tell(new GuessColorMsg(1), ActorRef.noSender());
                    break;
                case 2:
                    blueButton.setAlpha(0.4f);
                    playerHandler.sendEmptyMessageDelayed(4, 500);
                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, mApplication.getActorSystem())
                            .tell(new GuessColorMsg(2), ActorRef.noSender());
                    break;
                case 3:
                    yellowButton.setAlpha(0.4f);
                    playerHandler.sendEmptyMessageDelayed(4, 500);
                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, mApplication.getActorSystem())
                            .tell(new GuessColorMsg(3), ActorRef.noSender());
                    break;
                case 4 : redButton.setAlpha(1);
                    blueButton.setAlpha(1);
                    yellowButton.setAlpha(1);
                    greenButton.setAlpha(1);
                    break;

            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int radiobtnIndex = 0;
        if(savedInstanceState != null){
            radiobtnIndex = savedInstanceState.getInt(Constants.RADIOBTN_INDEX_KEY);
        }

        greenButton = (Button) findViewById(R.id.game_button1);
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playerTurn){

                    playerHandler.sendEmptyMessage(0);
                }
            }
        });
        redButton = (Button) findViewById(R.id.game_button2);
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playerTurn){
                    playerHandler.sendEmptyMessage(1);
                }
            }
        });
        blueButton = (Button) findViewById(R.id.game_button3);
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playerTurn){
                    playerHandler.sendEmptyMessage(2);
                }
            }
        });
        yellowButton = (Button) findViewById(R.id.game_button4);
        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playerTurn){
                    playerHandler.sendEmptyMessage(3);
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

    public Handler getActorHandler(){
        return this.actorHandler;
    }

    public void setPlayerTurn(boolean isPlayerTurn){
        this.playerTurn = isPlayerTurn;
    }

}
