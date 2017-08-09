package app.simone.scores.google;

import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;

/**
 * Created by sapi9 on 03/07/2017.
 */

public class MatchInitiatedCallback implements
        ResultCallback<TurnBasedMultiplayer.InitiateMatchResult> {

    @Override
    public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
        // Check if the status code is not success.
        Status status = result.getStatus();
        if (!status.isSuccess()) {
            Log.d("##MACHINITIATEDCALLBACK","errore!");
            return;
        }

        TurnBasedMatch match = result.getMatch();

        // If this player is not the first player in this match, continue.
        if (match.getData() != null) {
            Log.d("##MACHINITIATEDCALLBACK","WAITING FOR MY TURN");
            return;
        }

        // Otherwise, this is the first player. Initialize the game state.
        Log.d("##MACHINITIATEDCALLBACK","GAME STARTED");
        //initGame(match);



        // Let the player take the first turn
        Log.d("##MACHINITIATEDCALLBACK","showTurnUi");
        //showTurnUI(match);

    }


}