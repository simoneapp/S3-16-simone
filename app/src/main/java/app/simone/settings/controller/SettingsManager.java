package app.simone.settings.controller;

import android.content.Context;
import android.content.SharedPreferences;

import app.simone.shared.utils.Constants;

/**
 * Created by nicola on 24/08/2017.
 */

public class SettingsManager implements ISettingsManager {

    protected SharedPreferences prefs;

    public SettingsManager(Context context){
        this.prefs = context.getSharedPreferences(Constants.SETTINGS_KEY, Context.MODE_PRIVATE);
    }

    private boolean getBooleanPreference(String key) {
        return prefs.getBoolean(key, true);
    }

    private void setBooleanPreference(boolean enabled, String key) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, enabled);
        editor.apply();
    }

    public boolean isMusicEnabled() {
        return getBooleanPreference(Constants.MUSIC_KEY);
    }

    public void setMusicEnabled(boolean enabled) {
        setBooleanPreference(enabled, Constants.MUSIC_KEY);
    }

    public boolean areNotificationsEnabled() {
        return getBooleanPreference(Constants.NOTIFICATIONS_KEY);
    }

    public void setNotificationsEnabled(boolean enabled) {
        setBooleanPreference(enabled, Constants.NOTIFICATIONS_KEY);
    }
}
