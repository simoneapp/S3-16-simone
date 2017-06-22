package app.simone;

import android.os.Handler;

/**
 * Created by sapi9 on 20/06/2017.
 */

public interface IGameActivity {

    public Handler getViewHandler();
    public void enableButtons(boolean isEnabled);
}
