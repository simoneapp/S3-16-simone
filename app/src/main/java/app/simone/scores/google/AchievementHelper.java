package app.simone.scores.google;

import com.google.android.gms.games.Games;

import app.simone.R;
import app.simone.shared.application.App;
import app.simone.shared.utils.Constants;

/**
 * Created by sapi9 on 09/07/2017.
 */

public class AchievementHelper {


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
                achievement = mode == Constants.CLASSIC_MODE ? String.valueOf(R.string.achievement_double_rainbow ): String.valueOf(R.string.achievement_double_rainbow_hard);
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
        if(achievement!=null){
            Games.Achievements.unlockImmediate(App.getGoogleApiHelper().getGoogleApiClient(),
                    (achievement))
                    .setResultCallback(new AchievementCallback());
        }

    }
}
