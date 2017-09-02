package app.simone;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import app.simone.settings.controller.SettingsManager;

/**
 * Created by nicola on 25/08/2017.
 */

class TestUtils {

    static SettingsManager getManager() {
        return new SettingsManager(getContext());
    }

    static Context getContext() {
        return InstrumentationRegistry.getTargetContext();
    }
}
