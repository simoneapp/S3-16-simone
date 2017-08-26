package app.simone;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import app.simone.settings.controller.SettingsManager;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by nicola on 25/08/2017.
 */

@RunWith(AndroidJUnit4.class)
public class AudioManagerTest {

    @Test
    public void testPlayMusic() {
        enableMusic();
        TestableAudioManager instance = (TestableAudioManager) TestableAudioManager.Companion.getInstance();
        assertFalse(instance.getPlayer().isPlaying());
        instance.playSimoneMusic();
        assertTrue(instance.getPlayer().isPlaying());
        instance.stopSimoneMusic();
        assertFalse(instance.getPlayer().isPlaying());
    }

    public void testPreferencesEffectiveness() {
        disableMusic();
        TestableAudioManager instance = (TestableAudioManager) TestableAudioManager.Companion.getInstance();
        assertFalse(instance.getPlayer().isPlaying());
        instance.playSimoneMusic();
        assertFalse(instance.getPlayer().isPlaying());
        enableMusic();
        instance.playSimoneMusic();
        assertTrue(instance.getPlayer().isPlaying());
    }

    private void enableMusic() {
        SettingsManager manager = TestUtils.getManager();
        manager.setMusicEnabled(true);
    }

    private void disableMusic() {
        SettingsManager manager = TestUtils.getManager();
        manager.setMusicEnabled(true);
    }
}
