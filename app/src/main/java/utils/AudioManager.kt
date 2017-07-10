package utils

import android.content.Context
import app.simone.R
import com.facebook.FacebookSdk.getApplicationContext

/**
 * Created by nicola on 08/07/2017.
 */

class AudioManager private constructor() {

    val IS_DEBUG = true
    private val player = AudioPlayer()
    val pref = getApplicationContext().getSharedPreferences("PREF", Context.MODE_PRIVATE)

    private object Holder {
        val INSTANCE = AudioManager()
    }

    companion object {
        val instance: AudioManager by lazy { Holder.INSTANCE }
    }

    fun playSimoneMusic() {

        if(IS_DEBUG) return
        val value = pref.getBoolean("MUSIC", true)
        if(!value) return

        player.play(getApplicationContext(), R.raw.simonintro)
        player.mMediaPlayer.isLooping = true
    }

    fun stopSimoneMusic() {
        player.stop()
    }

}
