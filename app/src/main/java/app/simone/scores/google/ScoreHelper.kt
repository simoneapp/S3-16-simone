package app.simone.scores.google

import android.content.Context
import app.simone.scores.model.AchievementSpecifier
import app.simone.shared.application.App
import app.simone.shared.utils.Analytics
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
        achievementList.forEach {
            if(it.achievementId == score) {
                var stringId : Int? = null
                if(mode == Constants.CLASSIC_MODE) { stringId = it.classicServerId }
                else { stringId = it.hardServerId }

                if (App.getGoogleApiHelper().googleApiClient.isConnected) {
                    val finalID = context.resources.getString(stringId)
                    Analytics.logAchievement(finalID, getApplicationContext())
                    Games.Achievements.unlockImmediate(App.getGoogleApiHelper().googleApiClient,
                            finalID).setResultCallback(AchievementCallback())
                }
                return
            }
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

        achievementList.forEach {
            if (it.achievementId == nGames && App.getGoogleApiHelper().googleApiClient.isConnected) {
                val serverID = context.resources.getString(it.classicServerId)
                Analytics.logAchievement(serverID, getApplicationContext())
                Games.Achievements.unlockImmediate(App.getGoogleApiHelper().googleApiClient,
                        serverID).setResultCallback(AchievementCallback())
            }
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
