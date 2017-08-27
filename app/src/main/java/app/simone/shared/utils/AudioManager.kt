package app.simone.shared.utils

import android.content.Context
import app.simone.R
import app.simone.settings.controller.SettingsManager
import com.facebook.FacebookSdk.getApplicationContext

/**
 * Created by nicola on 08/07/2017.
 */

open class AudioManager {

    open val IS_DEBUG = false
    protected open val player = AudioPlayer()
    var settingsManager : SettingsManager? = null

    private object Holder {
        val INSTANCE = AudioManager()
    }

    companion object {
        val instance: AudioManager by lazy { Holder.INSTANCE }
    }

    fun playSimoneMusic() {
        playSimoneMusic(getApplicationContext())
    }

    fun playSimoneMusic(c : Context) {

        if(settingsManager == null) {
            settingsManager = SettingsManager(c)
        }

        if(IS_DEBUG) return
        val value = settingsManager?.isMusicEnabled
        if(value == null || !value) return

        player.play(c, R.raw.simonintro)
        player.mMediaPlayer.isLooping = true
    }

    fun stopSimoneMusic() {
        player.stop()
    }

}
