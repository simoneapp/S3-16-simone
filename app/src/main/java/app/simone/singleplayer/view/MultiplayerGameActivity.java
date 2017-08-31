package app.simone.singleplayer.view;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import akka.actor.ActorRef;
import app.simone.multiplayer.controller.DataManager;
import app.simone.multiplayer.model.OnlineMatch;
import app.simone.shared.application.App;
import app.simone.shared.utils.Constants;
import app.simone.shared.utils.Utilities;
import app.simone.singleplayer.messages.ComputeFullMultiplayerSequenceMsg;
import app.simone.singleplayer.messages.ReceivedSequenceMsg;
import app.simone.singleplayer.model.SimonColorImpl;

/**
 * MultiplayerGameActivity, the activity of the classic multiplayer.
 * Subclass of GameActivityImpl.
 *
 * @author Michele Sapignoli
 */

public class MultiplayerGameActivity extends GameActivityImpl {

    /**
     * Implementation of the abstract method setup() of GameActivityImpl.
     * Behaves differently dependently on which player is playing (first or second).
     */
    @Override
    void setup() {
        final GameActivity context = this;
        if (getIntent().hasExtra(Constants.MULTIPLAYER_MODE)) {
            this.key = getIntent().getExtras().getString(Constants.MATCH_KEY_S);
            this.whichPlayer = getIntent().getExtras().getString(Constants.WHICH_PLAYER);

            if (whichPlayer.equals(Constants.FIRST_PLAYER)) {
                Utilities.getActor(Constants.CPU_ACTOR_NAME, App.getInstance().getActorSystem())
                        .tell(new ComputeFullMultiplayerSequenceMsg(presenter, key, 4, false), ActorRef.noSender());
            } else if (whichPlayer.equals(Constants.SECOND_PLAYER)) {

                DataManager.Companion.getInstance().getDatabase().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        OnlineMatch match = dataSnapshot.child(key).getValue(OnlineMatch.class);
                        List<SimonColorImpl> sequenceToPlay = match.getSequence();

                        Utilities.getActor(Constants.CPU_ACTOR_NAME, App.getInstance().getActorSystem())
                                .tell(new ReceivedSequenceMsg(sequenceToPlay, presenter), ActorRef.noSender());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

            }
        }
    }

    /**
     * Implementation of the abstract method of super-class.
     * Saves data in the Firebase database.
     */
    @Override
    public void saveScore() {
        simoneTextView.setText(Constants.BACK_TO_MENU);
        simoneTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        simoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DataManager.Companion.getInstance().getDatabase().child(key).child(whichPlayer).child("score").setValue("" + presenter.getFinalScore());

    }
}
