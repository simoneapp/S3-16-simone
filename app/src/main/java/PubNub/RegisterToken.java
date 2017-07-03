package PubNub;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import app.simone.R;

/**
 * Created by Giacomo on 30/06/2017.
 */

public class RegisterToken extends IntentService {

    private String token;


    public RegisterToken(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
             token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getToken(){
        return token;
    }
}
