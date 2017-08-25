package app.simone.singleplayer.view;

import android.os.Handler;

/**
 * Interface implemented by GameActivityImpl.
 * @author Michele Sapignoli
 */

public interface GameActivity {
    /**
     * Returns the handler of the game activity, the only component able to exchange messages with actors.
     * @return handler
     */
    Handler getHandler();
}
