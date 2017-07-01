package app.simone;

import android.os.Handler;

import akka.actor.ActorRef;

/**
 * Created by sapi9 on 20/06/2017.
 */

public interface IGameActivity {

    Handler getOuterHandler();

    void setPlayerTurn(boolean isPlayerTurn);
}
