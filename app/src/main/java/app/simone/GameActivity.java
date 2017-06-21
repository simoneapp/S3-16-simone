package app.simone;

import android.os.Bundle;
import android.support.annotation.Nullable;

import akka.actor.ActorRef;
import application.mApplication;
import messages.AttachViewMsg;
import messages.StartGameVsCPUMsg;
import utils.Constants;
import utils.Utilities;

/**
 * Created by sapi9 on 21/06/2017.
 */

public class GameActivity extends FullscreenActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        Pass the instance of the GameView to ViewActor
         */
        ActorRef viewActor = Utilities.getActorByName(Constants.PATH_ACTOR + Constants.VIEW_ACTOR_NAME, mApplication.getActorSystem());
        viewActor.tell(new AttachViewMsg(this), ActorRef.noSender());
    }

    @Override
    protected void setSubclassContentView() {
        setContentView(R.layout.activity_game);
        mContentView = findViewById(R.id.game_fullscreen_content);
    }
}
