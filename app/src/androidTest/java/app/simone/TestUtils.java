package app.simone;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import app.simone.settings.controller.SettingsManager;

/**
 * Created by nicola on 25/08/2017.
 */

public class TestUtils {

    public static SettingsManager getManager() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        return new SettingsManager(appContext);
    }
}
