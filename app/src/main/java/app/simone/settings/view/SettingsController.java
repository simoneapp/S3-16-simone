package app.simone.settings.view;

import android.content.Context;
import android.content.SharedPreferences;
import app.simone.shared.utils.AudioManager;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Giacomo on 11/08/2017.
 */

public class SettingsController {

    private boolean musicOn;
    private Context context;

    public SettingsController(Context context) {

        this.context=context;

        try{
            this.setMusicOn(loadPreferences());
        }catch (Exception e){
            musicOn=true;
        }
    }

    public boolean isMusicOn() {
        return musicOn;
    }

    public void setMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
    }

    public void musicOnOff(){

        musicOn=!musicOn;
        if(musicOn){
            AudioManager.Companion.getInstance().playSimoneMusic();
        }else {
            AudioManager.Companion.getInstance().stopSimoneMusic();
        }
        savePreferences();
    }

    public void savePreferences(){
        SharedPreferences.Editor editor = context.getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putBoolean("music", musicOn);
        editor.apply();
    }

    public boolean loadPreferences(){
        SharedPreferences prefs = context.getSharedPreferences("Settings", MODE_PRIVATE);
        return prefs.getBoolean("music",true);
    }

}
