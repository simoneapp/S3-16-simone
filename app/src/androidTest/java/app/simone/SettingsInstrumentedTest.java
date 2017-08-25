package app.simone;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import app.simone.settings.controller.SettingsManager;

import static junit.framework.Assert.assertTrue;

/**
 * Created by nicola on 25/08/2017.
 */

@RunWith(AndroidJUnit4.class)
public class SettingsInstrumentedTest {

    @Test
    public void testNotificationsSettings(){
        SettingsManager manager = getManager();
        boolean current = manager.areNotificationsEnabled();
        manager.setNotificationsEnabled(!current);
        assertTrue(manager.areNotificationsEnabled() != current);
    }

    @Test
    public void testMusicSettings(){
        SettingsManager manager = getManager();
        boolean current = manager.isMusicEnabled();
        manager.setMusicEnabled(!current);
        assertTrue(manager.isMusicEnabled() != current);
    }

    private SettingsManager getManager() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        return new SettingsManager(appContext);
    }
}
