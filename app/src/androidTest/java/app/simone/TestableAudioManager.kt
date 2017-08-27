package app.simone

import app.simone.shared.utils.AudioManager
import app.simone.shared.utils.AudioPlayer

/**
 * Created by nicola on 25/08/2017.
 */

/**
 * An audio manager with the flag IS_DEBUG = false, in order to make the class testable
 * with instrumented tests but without affecting the flag while in debug or release.
 */

open class TestableAudioManager : AudioManager() {
    override val IS_DEBUG = false
    override val player = AudioPlayer()

    private object Holder {
        val INSTANCE = TestableAudioManager()
    }

    companion object {
        val instance: TestableAudioManager by lazy { Holder.INSTANCE }
    }
}

