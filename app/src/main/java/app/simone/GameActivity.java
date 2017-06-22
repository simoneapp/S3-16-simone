package app.simone;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Button;

import akka.actor.ActorRef;
import application.mApplication;
import messages.AttachViewMsg;
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    redButton.setAlpha(0.4f);
                    mHandler.sendEmptyMessageDelayed(5, 1000);
                    break;
                case 1:
                    greenButton.setAlpha(0.4f);
                    mHandler.sendEmptyMessageDelayed(5, 1000);
                    break;
                case 2:
                    yellowButton.setAlpha(0.4f);
                    mHandler.sendEmptyMessageDelayed(5, 1000);
                    break;
                case 3:
                    blueButton.setAlpha(0.4f);
                    mHandler.sendEmptyMessageDelayed(5, 1000);
                    break;
                case 5: redButton.setAlpha(1);
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
        redButton = (Button) findViewById(R.id.game_button2);
        blueButton = (Button) findViewById(R.id.game_button3);
        yellowButton = (Button) findViewById(R.id.game_button4);

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

    public Handler getViewHandler(){
        return this.mHandler;
    }

    public void enableButtons(boolean isEnabled){
        greenButton.setEnabled(isEnabled);
        redButton.setEnabled(isEnabled);
        yellowButton.setEnabled(isEnabled);
        blueButton.setEnabled(isEnabled);
    }

}
