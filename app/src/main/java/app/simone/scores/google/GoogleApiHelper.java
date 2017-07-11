package app.simone.scores.google;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import app.simone.shared.main.FullscreenBaseGameActivity;

/**
 * Created by sapi9 on 07/07/2017.
 */

public class GoogleApiHelper {
    private static final String TAG = GoogleApiHelper.class.getSimpleName();
    private Context context;
    private GoogleApiClient mGoogleApiClient;

    public GoogleApiHelper(Context context) {
        this.context = context;
    }

    public GoogleApiClient getGoogleApiClient() {
        return this.mGoogleApiClient;
    }



    public void disconnect() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
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

    public void buildGoogleApiClient(FullscreenBaseGameActivity activity, View view) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(activity)
                .addOnConnectionFailedListener(activity)
                .addScope(Games.SCOPE_GAMES)
                .setViewForPopups(view)
                .addApi(Games.API).build();
        mGoogleApiClient.connect();

    }


}
