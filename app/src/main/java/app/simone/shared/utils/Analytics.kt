package app.simone.shared.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics



/**
 * Created by nicola on 12/10/2017.
 */

enum class AnalyticsAppAction {
    SINGLE_PLAYER_TOUCHED,
    MULTI_PLAYER_TOUCHED,
    SETTINGS_TOUCHED,
    SCOREBOARD_TOUCHED,
    CREDITS_TOUCHED,
    GAME_START_TOUCHED
}

class Analytics {

    companion object {
        fun logAchievement(name: String, context: Context) {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT)
            bundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, name)
            log(context, bundle)
        }

        fun logAppOpen(context: Context) {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FirebaseAnalytics.Event.APP_OPEN)
            log(context, bundle)
        }

        fun logScore(score: Int, context: Context) {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, FirebaseAnalytics.Event.POST_SCORE)
            bundle.putString(FirebaseAnalytics.Param.SCORE, score.toString())
            log(context, bundle)
        }

        fun logAppAction(type: AnalyticsAppAction, context: Context) {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type.toString())
            log(context, bundle)
        }

        fun logNewSinglePlayerGame(type: Int, context: Context) {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "NEW_SINGLE_PLAYER_GAME")
            bundle.putString("SINGLE_PLAYER_TYPE", type.toString())
            log(context, bundle)
        }

        private fun log(context: Context, bundle: Bundle) {
            val analytics = FirebaseAnalytics.getInstance(context)
            analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }
    }
}