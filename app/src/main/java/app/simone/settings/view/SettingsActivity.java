package app.simone.settings.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import app.simone.R;
import app.simone.settings.controller.SettingsManager;
import app.simone.shared.main.FullscreenBaseGameActivity;
import app.simone.shared.utils.AudioManager;

/**
 *  This is the activity of the settings.
 *  It is made by two buttons, the first one is about sounds (on/off) while the other one is about push notification (on/off)
 *
 *  @author Giacomo
 */

public class SettingsActivity extends FullscreenBaseGameActivity {
    private SettingsManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new SettingsManager(this);
        showCurrentSettings();

        FloatingActionButton musicButton = (FloatingActionButton) this.findViewById(R.id.settings_music);
        musicButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                musicSwitch();
            }
        });

        /*FloatingActionButton notificationButton = (FloatingActionButton) this.findViewById(R.id.settings_notification);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                notificationsSwitch();
            }
        });*/

        FloatingActionButton creditsButton = (FloatingActionButton) this.findViewById(R.id.settings_credits);
        creditsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openCredits();
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MusicPref", 0);
        String s=pref.getString("STRING",null);
        if(s.equals("false")){
            setMusicText(false);
        }else if(s.equals("true")){
            setMusicText(true);
            //let the music keep playing
        }
    }

    @Override
    protected void setSubclassContentView() {
        setContentView(R.layout.activity_settings);
        mContentView = findViewById(R.id.settings_fullscreen_content);
    }

    @Override
    protected void backTransition() {
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    private void musicSwitch(){

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MusicPref", 0);
        String s=pref.getString("STRING",null);
        boolean enabled = manager.isMusicEnabled();
        enabled = !enabled;
        manager.setMusicEnabled(enabled);
        playAudio(enabled);
        SharedPreferences.Editor editor = pref.edit();
        if(!enabled){
            editor.putString("STRING", "false");
            editor.commit();
            setMusicText(false);
        }else{
            editor.putString("STRING", "true");
            editor.commit();
            setMusicText(true);
        }

    }

    private void openCredits() {
        Intent intent = new Intent(this,CreditsActivity.class);
        this.startActivity(intent);
    }

    private void showCurrentSettings() {
        boolean musicEnabled = manager.isMusicEnabled();
        setMusicText(musicEnabled);

        /*boolean notificationsEnabled = manager.areNotificationsEnabled();
        setNotificationsText(notificationsEnabled);*/
    }

    public void playAudio(boolean isMusicOn) {
        if(isMusicOn){
            AudioManager.Companion.getInstance().playSimoneMusic();
        }else {
            AudioManager.Companion.getInstance().stopSimoneMusic();
        }
    }

    private void setMusicText(boolean enabled){
        setBooleanText(enabled, R.id.music_text);
    }

    private void setBooleanText(boolean value, int viewID) {
        TextView textView = (TextView)findViewById(viewID);
        if(value){
            textView.setText(R.string.on);
        }else {
            textView.setText(R.string.off);
        }
    }


}


