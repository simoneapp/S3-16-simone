package app.simone.scores.google;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.achievement.Achievements;

/**
 * Achievement Callback class.
 * @author Michele Sapignoli
 */

public class AchievementCallback implements ResultCallback<Achievements.UpdateAchievementResult>{

    @Override
    public void onResult(@NonNull Achievements.UpdateAchievementResult updateAchievementResult) {
        Log.d("##AchievementCallback", updateAchievementResult.toString());
    }
}
