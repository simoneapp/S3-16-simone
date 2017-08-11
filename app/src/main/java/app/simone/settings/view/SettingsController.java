package app.simone.settings.view;

import android.content.Context;
import android.content.SharedPreferences;
import app.simone.shared.utils.AudioManager;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Giacomo on 11/08/2017.
 */

class SettingsController {

    private boolean musicOn;
    private Context context;

    SettingsController(Context context) {

        this.context=context;

        try{
            this.setMusicOn(loadPreferences());
        }catch (Exception e){
            musicOn=true;
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
        SharedPreferences.Editor editor = context.getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putBoolean("music", musicOn);
        editor.apply();
    }

    private boolean loadPreferences(){
        SharedPreferences prefs = context.getSharedPreferences("Settings", MODE_PRIVATE);
        return prefs.getBoolean("music",true);
    }

}
