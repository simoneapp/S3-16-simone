package app.simone.scores.google;

import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.leaderboard.Leaderboards;

/**
 * Leaderboard Callback class.
 * @author Michele Sapignoli
 */


public class LeaderboardCallback implements ResultCallback<Leaderboards.SubmitScoreResult> {
    @Override
    public void onResult(Leaderboards.SubmitScoreResult res) {
        if (res.getStatus().getStatusCode() == 0) {
            // data sent successfully to server.
            // display toast.
            Log.d("##LeaderboardCallback","SCORE SENT");
        }
    }

}
