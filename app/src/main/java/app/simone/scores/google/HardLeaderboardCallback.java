package app.simone.scores.google;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.leaderboard.Leaderboards;

import app.simone.shared.utils.Constants;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by sapi9 on 12/07/2017.
 */

public class HardLeaderboardCallback implements ResultCallback<Leaderboards.SubmitScoreResult> {

    @Override
    public void onResult(@NonNull Leaderboards.SubmitScoreResult res) {
        if (res.getStatus().getStatusCode() == 0) {
            // data sent successfully to server.
            // display toast.
            Log.d("##HardLeaderboardCall","SCORE SENT");
            final SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE);
            pref.edit().putInt(Constants.NEED_TO_SYNC_HARD, 0).apply();
        }
    }
}
