package app.simone.settings.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import app.simone.R;
import app.simone.shared.main.FullscreenBaseGameActivity;
import app.simone.shared.utils.AudioManager;


public class SettingsActivity extends FullscreenBaseGameActivity {
    
    private SettingsController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller=new SettingsController(this.getApplicationContext());

        setText(controller.isMusicOn());

        FloatingActionButton musicButton = (FloatingActionButton) this.findViewById(R.id.settings_music);
        musicButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               controller.musicOnOff();
               setText(controller.isMusicOn());
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


    private void setText(boolean onOff){
        TextView musicTxt = (TextView)findViewById(R.id.music_text);
        if(onOff){
            musicTxt.setText("on");
        }else {
            musicTxt.setText("off");
        }
    }

    @Override
    protected void onPause() {
        if(controller.isMusicOn()){
            controller.musicOnOff();
        }
        super.onPause();
    }

}


