package app.simone;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import akka.actor.ActorRef;
import application.mApplication;
import colors.Colors;
import messages.AttachViewMsg;
import messages.GuessColorMsg;
import messages.NextColorMsg;
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


    private Handler outerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Button b = (Button) findViewById(msg.what);
            b.setAlpha(0.4f);
            Message m = new Message();
            m.what = msg.what;
            m.arg1 = msg.arg1;
            viewHandler.sendMessageDelayed(m, 300);
        }
    };

    private Handler viewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Button b = (Button) findViewById(msg.what);
            b.setAlpha(1);

            switch (msg.arg1) {
                case Constants.CPU_TURN:
                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, mApplication.getActorSystem())
                            .tell(new NextColorMsg(), ActorRef.noSender());
                    break;
                case Constants.PLAYER_TURN:
                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, mApplication.getActorSystem())
                            .tell(new GuessColorMsg(Colors.fromInt(msg.what)), ActorRef.noSender());
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

        greenButton = (Button) findViewById(R.id.GREEN);
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerTurn) {
                    Message m = new Message();
                    m.what = Colors.GREEN.getValue();
                    m.arg1 = Constants.PLAYER_TURN;
                    outerHandler.sendMessage(m);
                }
            }
        });
        redButton = (Button) findViewById(R.id.RED);
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerTurn) {
                    Message m = new Message();
                    m.what = Colors.RED.getValue();
                    m.arg1 = Constants.PLAYER_TURN;
                    outerHandler.sendMessage(m);
                }
            }
        });
        blueButton = (Button) findViewById(R.id.BLUE);
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerTurn) {
                    Message m = new Message();
                    m.what = Colors.BLUE.getValue();
                    m.arg1 = Constants.PLAYER_TURN;
                    outerHandler.sendMessage(m);
                }
            }
        });
        yellowButton = (Button) findViewById(R.id.YELLOW);
        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerTurn) {
                    Message m = new Message();
                    m.what = Colors.YELLOW.getValue();
                    m.arg1 = Constants.PLAYER_TURN;
                    outerHandler.sendMessage(m);
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

    public Handler getOuterHandler() {
        return this.outerHandler;
    }

    public void setPlayerTurn(boolean isPlayerTurn) {
        this.playerTurn = isPlayerTurn;
    }


}
