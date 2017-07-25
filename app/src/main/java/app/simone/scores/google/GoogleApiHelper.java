package app.simone.scores.google;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

/**
 * Created by sapi9 on 07/07/2017.
 */

public class GoogleApiHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = GoogleApiHelper.class.getSimpleName();
    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private Activity resolutionActivity;

    public GoogleApiHelper(Context context) {
        this.context = context;
    }

    public GoogleApiClient getGoogleApiClient() {
        return this.mGoogleApiClient;
    }

    public void connect() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public void disconnect() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    public boolean isConnected() {
        if (mGoogleApiClient != null) {
            return mGoogleApiClient.isConnected();
        } else {
            return false;
        }
    }

    public void buildGoogleApiClient(View view, Activity activity) {
        resolutionActivity = activity;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addScope(Games.SCOPE_GAMES)
                .setViewForPopups(view)
                .addApi(Games.API).build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("##"+TAG, "connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("##" + TAG, "onConnectionSuspended: googleApiClient.connect()");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("##" + TAG, "onConnectionFailed: connectionResult.toString() = " + connectionResult.toString());
        if (connectionResult.hasResolution()) {
            try {
                // !!!
                connectionResult.startResolutionForResult(resolutionActivity, 1);
            } catch (IntentSender.SendIntentException e) {
                this.connect();
            }
        }
    }
}
