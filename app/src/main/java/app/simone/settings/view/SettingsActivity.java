package app.simone.settings.view;

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
        boolean enabled = manager.isMusicEnabled();
        enabled = !enabled;
        manager.setMusicEnabled(enabled);
        playAudio(enabled);
        showCurrentSettings();
    }

    /*private void notificationsSwitch() {
        boolean enabled = manager.areNotificationsEnabled();
        enabled = !enabled;
        manager.setNotificationsEnabled(enabled);
        showCurrentSettings();
    }*/

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

   /* private void setNotificationsText(boolean enabled){
        setBooleanText(enabled, R.id.notification_text);
    }*/

    private void setBooleanText(boolean value, int viewID) {
        TextView textView = (TextView)findViewById(viewID);
        if(value){
            textView.setText(R.string.on);
        }else {
            textView.setText(R.string.off);
        }
    }

    @Override
    protected void onPause() {
        if(manager.isMusicEnabled()){
            AudioManager.Companion.getInstance().stopSimoneMusic();
        }
        super.onPause();
    }

}


