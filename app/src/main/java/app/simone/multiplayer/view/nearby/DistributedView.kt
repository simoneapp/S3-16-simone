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

        fun updateButtonBlink(blinkTonality: Float)

        fun getHandler(): Handler?

        fun updateColor(color: SimonColor)

        fun startGame()

        fun showMessage(text: String)

    }

    interface WaitingRoomView {
        fun getIntent(): Intent
        fun updateText(text:String)
        fun getActivityContext():WaitingRoomActivity
    }
}