package app.simone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;


import PubNub.PubnubController;

public class PubnubActivity extends AppCompatActivity {

    private Button subscribeButton;
    private Button publishButton;
    private Button unscribeButton;
    private TextView msgView;
    private PubnubController myPub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubnub);

        subscribeButton = (Button)findViewById(R.id.sButton);
        publishButton = (Button)findViewById(R.id.pButton);
        unscribeButton = (Button)findViewById(R.id.unButton);
        msgView = (TextView) findViewById(R.id.label);

        myPub = new PubnubController("myChannel");

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
                myPub.publishToChannel("ciao Sapi");
            }
        });

        unscribeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                System.out.println("unscribed from channel");
                myPub.unscribe();
            }
        });
    }

}


