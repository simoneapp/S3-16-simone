package app.simone.singleplayer.view;

import android.os.Handler;

/**
 * Created by sapi9 on 20/06/2017.
 */

public interface IGameActivity {

    Handler getHandler();
    void setPlayerBlinking(boolean isPlayerTurn);
}
