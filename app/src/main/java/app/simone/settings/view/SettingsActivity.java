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

    private boolean musicOn=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
        TextView musicTxt = (TextView)findViewById(R.id.music_text);
        musicOn=!musicOn;
        if(musicOn){
            AudioManager.Companion.getInstance().playSimoneMusic();
            musicTxt.setText("on");
        }else {
            AudioManager.Companion.getInstance().stopSimoneMusic();
            musicTxt.setText("off");
        }
        // we still have to save this value into the sharedPreferences!
    }

}


