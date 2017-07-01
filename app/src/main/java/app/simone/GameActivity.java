package app.simone;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import akka.actor.ActorRef;
import app.simone.Controller.ControllerImplementations.UserDataAccessControllerImpl;
import app.simone.Controller.ControllerImplementations.UserMatchControllerImpl;
import app.simone.Controller.UserMatchController;
import application.mApplication;
import colors.Color;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import messages.AttachViewMsg;
import messages.GuessColorMsg;
import messages.NextColorMsg;
import utils.Constants;
import utils.Utilities;

/**
 * @author Michele Sapignoli
 */

public class GameActivity extends FullscreenActivity implements IGameActivity {
    private boolean playerTurn;
    private Realm realm;


    private Handler outerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == Constants.GAME_OVER) {
                UserMatchController controller=new UserMatchControllerImpl(realm);
                controller.insertMatch("Michele Sapignoli",msg.what);
                Log.d("REALM","score received: "+new UserDataAccessControllerImpl(realm).getMatches("Michele Sapignoli").first().toString());

            }
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
                            .tell(new GuessColorMsg(Color.fromInt(msg.what)), ActorRef.noSender());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRealm();
        int radiobtnIndex = 0;

        if (savedInstanceState != null) {
            radiobtnIndex = savedInstanceState.getInt(Constants.RADIOBTN_INDEX_KEY);
        }

        initButton(Color.GREEN);
        initButton(Color.RED);
        initButton(Color.YELLOW);
        initButton(Color.BLUE);

        /*
        Pass the instance of the GameActivity to GameViewActor
         */

        Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, mApplication.getActorSystem())
                .tell(new AttachViewMsg(this, radiobtnIndex), ActorRef.noSender());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void setSubclassContentView() {
        setContentView(R.layout.activity_game);
        mContentView = findViewById(R.id.game_fullscreen_content);
    }

    private boolean initButton(final Color color) {
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

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("DBPlayers.realm").deleteRealmIfMigrationNeeded().schemaVersion(5).build();
        Realm.setDefaultConfiguration(config);
        realm=Realm.getDefaultInstance();
    }

    public Handler getOuterHandler() {
        return this.outerHandler;
    }

    public void setPlayerTurn(boolean isPlayerTurn) {
        this.playerTurn = isPlayerTurn;
    }


}
