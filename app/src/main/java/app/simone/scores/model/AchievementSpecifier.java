package app.simone.scores.model;

import java.util.Arrays;
import java.util.List;

import app.simone.R;
import app.simone.shared.utils.Constants;

/**
 * Created by nicola on 01/09/2017.
 */

public class AchievementSpecifier {

    private List<Achievement> badgeAchievements;
    private List<Achievement> nGamesAchievements;
    private static AchievementSpecifier instance;

    private AchievementSpecifier() {

        this.badgeAchievements = Arrays.asList(
                new Achievement(Constants.ACHIEVEMENT_SEQ_1, R.string.achievement_rgb, R.string.achievement_rgb_hard),
                new Achievement(Constants.ACHIEVEMENT_SEQ_2, R.string.achievement_rainbow, R.string.achievement_rainbow_hard),
                new Achievement(Constants.ACHIEVEMENT_SEQ_3, R.string.achievement_double_rainbow, R.string.achievement_double_rainbow_hard),
                new Achievement(Constants.ACHIEVEMENT_SEQ_4, R.string.achievement_head_full_of_dreams, R.string.achievement_head_full_of_dreams_hard),
                new Achievement(Constants.ACHIEVEMENT_SEQ_5, R.string.achievement_deca, R.string.achievement_deca_hard),
                new Achievement(Constants.ACHIEVEMENT_SEQ_6, R.string.achievement_meaning_of_life, R.string.achievement_meaning_of_life_hard),
                new Achievement(Constants.ACHIEVEMENT_SEQ_7, R.string.achievement_cheater, R.string.achievement_master_of_cheating),
                new Achievement(Constants.ACHIEVEMENT_SEQ_8, R.string.achievement_i_have_nothing_to_do_in_my_life, R.string.achievement_i_have_nothing_to_do_in_my_life));

        this.nGamesAchievements = Arrays.asList(
                new Achievement(Constants.FIVE, R.string.achievement_cimabue),
                new Achievement(Constants.TWENTYFIVE, R.string.achievement_van_gogh),
                new Achievement(Constants.FIFTY, R.string.achievement_giotto),
                new Achievement(Constants.SEVENTYFIVE, R.string.achievement_paniccia),
                new Achievement(Constants.HUNDRED, R.string.achievement_simon),
                new Achievement(Constants.TWOHUNDREDFIFTY, R.string.achievement_nyancat),
                new Achievement(Constants.FIVEHUNDRED, R.string.achievement_unicorn),
                new Achievement(Constants.THOUSAND, R.string.achievement_giuliano_e_i_notturni)
        );
    }


    public static AchievementSpecifier getInstance() {
        if(instance == null){
            instance = new AchievementSpecifier();
        }
        return instance;
    }

    public List<Achievement> getnGamesAchievements() {
        return nGamesAchievements;
    }

    public List<Achievement> getBadgeAchievements() {
        return badgeAchievements;
    }
}
