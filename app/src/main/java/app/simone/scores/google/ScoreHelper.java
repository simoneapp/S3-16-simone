package app.simone.scores.google;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.games.Games;

import app.simone.R;
import app.simone.shared.application.App;
import app.simone.shared.utils.Constants;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by sapi9 on 09/07/2017.
 */

public class ScoreHelper {


    public static void checkAchievement(int score, int mode) {
        String achievement = null;

        switch (score) {
            case Constants.ACHIEVEMENT_SEQ_1:
                achievement = (mode == Constants.CLASSIC_MODE ? String.valueOf(R.string.achievement_rgb) : String.valueOf(R.string.achievement_rgb_hard));
                break;
            case Constants.ACHIEVEMENT_SEQ_2:
                achievement = mode == Constants.CLASSIC_MODE ? String.valueOf(R.string.achievement_rainbow) : String.valueOf(R.string.achievement_rainbow_hard);
                break;
            case Constants.ACHIEVEMENT_SEQ_3:
                achievement = mode == Constants.CLASSIC_MODE ? String.valueOf(R.string.achievement_double_rainbow) : String.valueOf(R.string.achievement_double_rainbow_hard);
                break;
            case Constants.ACHIEVEMENT_SEQ_4:
                achievement = mode == Constants.CLASSIC_MODE ? String.valueOf(R.string.achievement_head_full_of_dreams) : String.valueOf(R.string.achievement_head_full_of_dreams_hard);
                break;
            case Constants.ACHIEVEMENT_SEQ_5:
                achievement = mode == Constants.CLASSIC_MODE ? String.valueOf(R.string.achievement_hard_as_zync) : String.valueOf(R.string.achievement_hard_as_zync_hard);
                break;
            case Constants.ACHIEVEMENT_SEQ_6:
                achievement = mode == Constants.CLASSIC_MODE ? String.valueOf(R.string.achievement_meaning_of_life) : String.valueOf(R.string.achievement_meaning_of_life_hard);
                break;
            case Constants.ACHIEVEMENT_SEQ_7:
                achievement = mode == Constants.CLASSIC_MODE ? String.valueOf(R.string.achievement_cheater) : String.valueOf(R.string.achievement_master_of_cheating);
                break;
            case Constants.ACHIEVEMENT_SEQ_8:
                achievement = mode == Constants.CLASSIC_MODE ? String.valueOf(R.string.achievement_i_have_nothing_to_do_in_my_life) : String.valueOf(R.string.achievement_terry);
                break;
        }
        if (achievement != null) {
            if (App.getGoogleApiHelper().getGoogleApiClient().isConnected()) {
                Games.Achievements.unlockImmediate(App.getGoogleApiHelper().getGoogleApiClient(),
                        (achievement))
                        .setResultCallback(new AchievementCallback());
            } else {
                //TODO WRITE SU SHARED
            }
        }

    }

    public static void sendResultToLeaderboard(int chosenMode, int finalScore) {
        if (App.getGoogleApiHelper().isConnected()) {
            Games.Leaderboards.submitScoreImmediate(App.getGoogleApiHelper().getGoogleApiClient(),
                    chosenMode == Constants.CLASSIC_MODE ? Constants.LEADERBOARD_CLASSIC_ID : Constants.LEADERBOARD_HARD_ID, finalScore)
                    .setResultCallback(new LeaderboardCallback());
        } else {
            final SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE);
            int stored = pref.getInt(chosenMode == 0 ? Constants.PREF_KEY_LEAD_CLASSIC : Constants.PREF_KEY_LEAD_HARD, 0);
            if (finalScore > stored) {

                pref.edit().putInt(chosenMode == 0 ? Constants.NEED_TO_SYNC_CLASSIC : Constants.NEED_TO_SYNC_HARD, 1).apply();
                pref.edit().putInt(chosenMode == 0 ? Constants.PREF_KEY_LEAD_CLASSIC : Constants.PREF_KEY_LEAD_HARD, finalScore).apply();
            }
        }
    }
}
