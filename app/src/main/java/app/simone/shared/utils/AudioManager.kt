package app.simone.shared.utils

import app.simone.R
import app.simone.settings.controller.SettingsManager
import com.facebook.FacebookSdk.getApplicationContext

/**
 * Created by nicola on 08/07/2017.
 */

open class AudioManager {

    open val IS_DEBUG = false
    protected open val player = AudioPlayer()
    val settingsManager = SettingsManager(getApplicationContext())

    private object Holder {
        val INSTANCE = AudioManager()
    }

    companion object {
        val instance: AudioManager by lazy { Holder.INSTANCE }
    }

    fun playSimoneMusic() {

        if(IS_DEBUG) return
        val value = settingsManager.isMusicEnabled
        if(!value) return

        player.play(getApplicationContext(), R.raw.simonintro)
        player.mMediaPlayer.isLooping = true
    }

    fun stopSimoneMusic() {
        player.stop()
    }

}
