package app.simone.settings.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import app.simone.shared.utils.AudioManager;

/**
 * Created by Giacomo on 11/08/2017.
 */

class SettingsController {

    private boolean musicOn;
    private Context context;
    private SharedPreferences editor;

    SettingsController(Context context) {

        this.context=context;
        editor=PreferenceManager.getDefaultSharedPreferences(context);
        try {
            this.setMusicOn(loadPreferences());
        }catch (Exception e){
            //musicOn=false;
        }

    }

    boolean isMusicOn() {
        return musicOn;
    }

    private void setMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
    }

    void musicOnOff(){

        musicOn=!musicOn;
        if(musicOn){
            AudioManager.Companion.getInstance().playSimoneMusic();
        }else {
            AudioManager.Companion.getInstance().stopSimoneMusic();
        }
        savePreferences();
    }

    private void savePreferences(){
        editor.edit().putBoolean("music", musicOn).apply();
    }

    private boolean loadPreferences(){
        return editor.getBoolean("music",true);
    }

}
