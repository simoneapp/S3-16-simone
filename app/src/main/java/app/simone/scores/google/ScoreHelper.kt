package app.simone.scores.google

import android.content.Context
import app.simone.scores.model.AchievementSpecifier
import app.simone.shared.application.App
import app.simone.shared.utils.Constants
import com.facebook.FacebookSdk.getApplicationContext
import com.google.android.gms.games.Games

/**
 * ScoreHelper class.
 * @author Michele Sapignoli
 */

object ScoreHelper {

    /**
     * Checks if the given score is associated with a Google Games achievement.
     * @param score
     * *
     * @param mode
     */
    fun checkAchievement(context: Context, score: Int, mode: Int) {

        val achievementList = AchievementSpecifier.getInstance().badgeAchievements
        val achievement = achievementList.stream().filter { it.achievementId == score }.findFirst()

        if(!achievement.isPresent) return

        var stringId : Int? = null
        if(mode == Constants.CLASSIC_MODE) { stringId = achievement.get().classicServerId }
        else { stringId = achievement.get().hardServerId }

        if (achievement != null && App.getGoogleApiHelper().googleApiClient.isConnected) {
                Games.Achievements.unlockImmediate(App.getGoogleApiHelper().googleApiClient,
                        context.resources.getString(stringId))
                        .setResultCallback(AchievementCallback())
        }
    }

    /**
     * Checks if number of matches played is associated with a Google Games achievement.
     */
    fun checkNGamesAchievement(context: Context) {
        val pref = getApplicationContext().getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE)
        var nGames = pref.getInt(Constants.N_GAMES, 0)
        nGames++

        val editor = pref.edit()
        editor.putInt(Constants.N_GAMES, nGames)
        editor.apply()

        val achievementList = AchievementSpecifier.getInstance().getnGamesAchievements()
        val achievement = achievementList.stream().filter { it.achievementId == nGames }.findFirst()

        if(!achievement.isPresent) return

        if (achievement != null && App.getGoogleApiHelper().googleApiClient.isConnected) {
                Games.Achievements.unlockImmediate(App.getGoogleApiHelper().googleApiClient,
                        context.resources.getString(achievement.get().classicServerId))
                        .setResultCallback(AchievementCallback())
        }
    }

    /**
     * Method that sends the highscore to the corresponding leaderboard
     * @param chosenMode
     * *
     * @param finalScore
     */
    fun sendResultToLeaderboard(context: Context, chosenMode: Int, finalScore: Int) {
        if (App.getGoogleApiHelper().isConnected) {
            Games.Leaderboards.submitScoreImmediate(App.getGoogleApiHelper().googleApiClient,
                    if (chosenMode == Constants.CLASSIC_MODE) Constants.LEADERBOARD_CLASSIC_ID else Constants.LEADERBOARD_HARD_ID, finalScore.toLong())
                    .setResultCallback(LeaderboardCallback())
        } else {
            val pref = context.getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE)
            val stored = pref.getInt(if (chosenMode == 0) Constants.PREF_KEY_LEAD_CLASSIC else Constants.PREF_KEY_LEAD_HARD, 0)
            if (finalScore > stored) {
                pref.edit().putInt(if (chosenMode == 0) Constants.NEED_TO_SYNC_CLASSIC else Constants.NEED_TO_SYNC_HARD, 1).apply()
                pref.edit().putInt(if (chosenMode == 0) Constants.PREF_KEY_LEAD_CLASSIC else Constants.PREF_KEY_LEAD_HARD, finalScore).apply()
            }
        }
    }
}
