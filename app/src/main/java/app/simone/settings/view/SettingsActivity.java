package app.simone.settings.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import app.simone.shared.utils.AudioManager;

public class SettingsActivity extends AppCompatActivity {

    private Button subscribeButton;
    private Button publishButton;
    private Button unscribeButton;
    private Button saveButton;
    private Switch swcMusic;
    private TextView msgView;
    private EditText editText;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        subscribeButton = (Button)findViewById(R.id.sButton);
        publishButton = (Button)findViewById(R.id.pButton);
        unscribeButton = (Button)findViewById(R.id.unButton);
        msgView = (TextView) findViewById(R.id.label);
        editText = (EditText)findViewById(R.id.username);
        saveButton = (Button) findViewById(R.id.saveButton);
        swcMusic = (Switch) findViewById(R.id.swcMusic);


        final SharedPreferences pref = this.getSharedPreferences("PREF", Context.MODE_PRIVATE);
        boolean value = pref.getBoolean("MUSIC", true);
        swcMusic.setChecked(value);
        swcMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                pref.edit().putBoolean("MUSIC", b).apply();
                if(b) {
                    AudioManager.Companion.getInstance().playSimoneMusic();
                } else {
                    AudioManager.Companion.getInstance().stopSimoneMusic();
                }
            }
        });



    }

}


