package app.simone.settings.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import app.simone.R;
import app.simone.shared.main.FullscreenBaseGameActivity;
import app.simone.shared.utils.AudioManager;
import app.simone.shared.utils.Constants;
import app.simone.singleplayer.view.GameActivity;

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
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putBoolean("music", musicOn);
        editor.apply();
    }

    private boolean loadPreferences(){
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        return prefs.getBoolean("music",true);
    }

    private void setText(boolean onOff){
        TextView musicTxt = (TextView)findViewById(R.id.music_text);
        if(onOff){
            musicTxt.setText("on");
        }else {
            musicTxt.setText("off");
        }
    }

}


