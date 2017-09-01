package app.simone.multiplayer.view.nearby

import android.content.Intent
import android.os.Handler
import app.simone.singleplayer.model.SimonColor

/**
 * Created by gzano on 31/08/2017.
 */
interface DistributedView {

    interface NearbyView {


        fun updateButtonText(text: String)

        /**
         *updates button color tonality for blink rendering
         * @param blinkTonality the tonality of button color
         */
        fun updateButtonBlink(blinkTonality: Float)

        /**
         * get the handler from activity
         * @return a hahndler
         */
        fun getHandler(): Handler?

        /**
         * updates button color as the match starts
         * @param SimonColor set the button background color
         */
        fun updateColor(color: SimonColor)

        /**
         * it starts the game
         */
        fun startGame()

        /**
         * allows the view to visualize a message
         * @param text to show the user
         */
        fun showMessage(text: String)

    }

    interface WaitingRoomView {
        /**
         * get the intent from the view
         */
        fun getIntent(): Intent

        /**
         * update a view widget text
         * @param text to show the user
         */
        fun updateText(text: String)

        /**
         * get the activity context
         * @return the waiting room activity context
         */
        fun getActivityContext(): WaitingRoomActivity

        /**
         * used to start the activity
         */
        fun startGameActivity(matchID: String)
    }
}