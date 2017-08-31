package app.simone.singleplayer.view;

import android.os.Message;
import android.view.animation.Animation;

public interface GameActivity {
    void renderYouLost(int score);
    void updateSimoneTextview(String text, Animation animation);
    void blinkDelayed(Message msg);
    void resetButton(int buttonId);
    void prepareMultiplayer();

    void swapButtonPositions();
    boolean isPlayerBlinking();
    void saveScore();
}