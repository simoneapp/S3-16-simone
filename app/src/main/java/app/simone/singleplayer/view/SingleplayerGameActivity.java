package app.simone.singleplayer.view;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;

import akka.actor.ActorRef;
import app.simone.shared.application.App;
import app.simone.shared.utils.AnimationHandler;
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

                if (tapToBegin) {
                    scoreText.setVisibility(View.GONE);
                    scoreButton.setVisibility(View.GONE);
                    gameFab.setEnabled(false);
                    tapToBegin = false;
                    finalScore = 0;
                    playerBlinking = false;
                    simoneTextView.startAnimation(AnimationHandler.getGameButtonAnimation());
                    simoneTextView.setText(Constants.STRING_EMPTY);
                    simoneTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#737373")));
                    gameFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f2f2f2")));

                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.CPU_ACTOR_NAME, App.getInstance().getActorSystem())
                            .tell(new StartGameVsCPUMsg(true), ActorRef.noSender());

                }
            }
        });

    }

    @Override
    void saveScore() {
        simoneTextView.setText(Constants.PLAY_AGAIN);
        simoneTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
    }
}
