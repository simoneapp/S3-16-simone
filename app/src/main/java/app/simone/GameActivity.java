package app.simone;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import akka.actor.ActorRef;
import app.simone.styleable.SimoneTextView;
import application.mApplication;
import colors.Color;
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

    private Handler outerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case Constants.SET_TURN_MSG:
                    viewHandler.sendEmptyMessage(msg.what == Constants.CPU_TURN? Constants.CPU_TURN : Constants.PLAYER_TURN);
                    break;
                default:
                    Button b = (Button) findViewById(msg.what);
                    b.setAlpha(0.4f);
                    Message m = new Message();
                    m.what = msg.what;
                    m.arg1 = msg.arg1;
                    viewHandler.sendMessageDelayed(m, 300);
                    break;

            }

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
                            .tell(new GuessColorMsg(Color.fromInt(msg.what)), ActorRef.noSender());
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

        initColorButton(Color.GREEN);
        initColorButton(Color.RED);
        initColorButton(Color.YELLOW);
        initColorButton(Color.BLUE);

        FloatingActionButton gameFab = (FloatingActionButton) findViewById(R.id.game_fab);
        simoneTextView = (SimoneTextView) findViewById(R.id.game_simone_textview);
        gameFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tapToBegin) {
                    tapToBegin = false;
                    simoneTextView.setText(Constants.STRING_EMPTY);
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

    private boolean initColorButton(final Color color) {
        Button button = (Button) findViewById(color.getValue());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerTurn) {
                    Message m = new Message();
                    m.what = color.getValue();
                    m.arg1 = Constants.PLAYER_TURN;
                    outerHandler.sendMessage(m);
                }
            }
        });
        return true;
    }

    public Handler getOuterHandler() {
        return this.outerHandler;
    }

    public void setPlayerTurn(boolean isPlayerTurn) {
        this.playerTurn = isPlayerTurn;
    }

}
