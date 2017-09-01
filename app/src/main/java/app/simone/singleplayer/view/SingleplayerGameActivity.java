package app.simone.singleplayer.view;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;

import app.simone.shared.application.App;
import app.simone.shared.utils.AudioManager;
import app.simone.shared.utils.Constants;
import app.simone.shared.utils.Utilities;
import app.simone.singleplayer.messages.StartGameVsCPUMsg;

/**
 * SingleplayerGameActivity, the activity of the single play.
 * Subclass of GameActivityImpl.
 *
 * @author Michele Sapignoli
 */
public class SingleplayerGameActivity extends GameActivityImpl {

    /**
     * Implementation of the abstract method setup() of GameActivityImpl.
     * Sets the listener on the game fab and the initial properties.
     * Calls the CPUActor to begin the game.
     */
    @Override
    void setup() {
        gameFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AudioManager.Companion.getInstance().stopSimoneMusic();

                if (presenter.isTapToBegin()) {
                    prepareViewsForGame();
                    presenter.prepareGame(new StartGameVsCPUMsg(true));
                }
            }
        });

    }

    @Override
    public void saveScore() {
        simoneTextView.setText(Constants.PLAY_AGAIN);
        simoneTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
    }

    @Override
    public void prepareGame() {
        presenter.prepareGame(new StartGameVsCPUMsg(true, Utilities.getActor(Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().getActorSystem())));
    }
}

