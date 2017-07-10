package utils.google;

import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.leaderboard.Leaderboards;

/**
 * Created by sapi9 on 07/07/2017.
 */


public class LeaderboardCallback implements ResultCallback<Leaderboards.SubmitScoreResult> {
    @Override
    public void onResult(Leaderboards.SubmitScoreResult res) {
        if (res.getStatus().getStatusCode() == 0) {
            // data sent successfully to server.
            // display toast.
            Log.d("##LeaderboardCallback","SCORE SENT");
            //TODO SPARARE TUTTI I PENDING
        } else {
            //TODO SALVARE
        }
    }
}