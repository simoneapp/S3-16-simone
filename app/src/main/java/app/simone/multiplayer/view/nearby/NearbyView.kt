package app.simone.multiplayer.view.nearby

import android.os.Handler
import app.simone.singleplayer.model.SColor

/**
 * Created by gzano on 24/08/2017.
 */
interface NearbyView {

    fun listenOnCpuIndexChange()

    fun updateButtonText(text:String)

    fun updateButtonBlink(blinkTonality:Float)

    fun getHandler(): Handler?

    fun updateColor(color: SColor)

}