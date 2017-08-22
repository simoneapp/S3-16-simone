package app.simone.settings.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import app.simone.R;
import app.simone.shared.main.FullscreenBaseGameActivity;
import app.simone.shared.utils.AudioManager;


public class SettingsActivity extends FullscreenBaseGameActivity {

    private boolean musicOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       try{
          musicOn=loadPreferences();
       }catch (Exception e){
           musicOn=true;
       }
        setText(musicOn);

        FloatingActionButton musicButton = (FloatingActionButton) this.findViewById(R.id.settings_music);
        musicButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               musicOnOff();
            }
        });

        FloatingActionButton notificationButton = (FloatingActionButton) this.findViewById(R.id.settings_notification);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTON","push");
            }
        });

    }

    @Override
    protected void setSubclassContentView() {
        setContentView(R.layout.activity_settings);
        mContentView = findViewById(R.id.settings_fullscreen_content);
    }

    @Override
    protected void backTransition() {
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private void musicOnOff(){

        musicOn=!musicOn;
        if(musicOn){
            AudioManager.Companion.getInstance().playSimoneMusic();
        }else {
            AudioManager.Companion.getInstance().stopSimoneMusic();
        }
        setText(musicOn);
        savePreferences();
    }

    private void savePreferences(){
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.settings_uc), MODE_PRIVATE).edit();
        editor.putBoolean(getString(R.string.music), musicOn);
        editor.apply();
    }

    private boolean loadPreferences(){
        SharedPreferences prefs = getSharedPreferences(getString(R.string.settings_uc), MODE_PRIVATE);
        return prefs.getBoolean(getString(R.string.music), true);
    }

    private void setText(boolean onOff){
        TextView musicTxt = (TextView)findViewById(R.id.music_text);
        if(onOff){
            musicTxt.setText(R.string.on);
        }else {
            musicTxt.setText(R.string.off);
        }
    }

}


