package app.simone.settings.controller;

/**
 * Created by nicola on 25/08/2017.
 */

public interface ISettingsManager {
    boolean isMusicEnabled();
    void setMusicEnabled(boolean enabled);
    boolean areNotificationsEnabled();
    void setNotificationsEnabled(boolean enabled);
}
