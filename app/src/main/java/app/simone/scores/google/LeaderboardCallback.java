package app.simone.scores.google;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.Leaderboards;

import app.simone.shared.application.App;

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
        }
    }

}
