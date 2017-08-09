package app.simone.settings.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

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
/*
        myPub = new PubnubController("myChannel");
*/
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
/*
        subscribeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                System.out.println("subscribe");
                Toast.makeText(getApplicationContext(), "subscribed to channel", Toast.LENGTH_SHORT).show();
                myPub.subscribeToChannel();

                myPub.getPubnub().addListener(new SubscribeCallback() {
                    @Override
                    public void status(PubNub pubnub, PNStatus status) {

                    }

                    @Override
                    public void message(PubNub pubnub, PNMessageResult message) {
                        if (message.getChannel() != null){
                            final String msg = message.getMessage().getAsString();


                            runOnUiThread(new Runnable() {
                                public void run() {
                                    msgView.setText(msg);
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

                    }
                });
            }
        });

        publishButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                System.out.println("publish");
                Toast.makeText(getApplicationContext(), "Msg 'ciao' published on channel", Toast.LENGTH_SHORT).show();
                //myPub.publishToChannel("ciao Sapi");
            }
        });

        unscribeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                System.out.println("unscribed from channel");
                myPub.unscribe();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                System.out.println("editText saved");
                username=String.valueOf(editText.getText());
            }
        });*/
    }

}


