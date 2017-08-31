package app.simone.scores.google;

import android.content.Context;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import app.simone.shared.main.FullscreenBaseGameActivity;

/**
 * GoogleApiHelper class.
 * @author Michele Sapignoli
 */

public class GoogleApiHelper {
    private Context context;
    private GoogleApiClient mGoogleApiClient;

    public GoogleApiHelper(Context context) {
        this.context = context;
    }

    public GoogleApiClient getGoogleApiClient() {
        return this.mGoogleApiClient;
    }


    /**
     * Method to check if GoogleApiHelper is connected.
     * @return mGoogleApiClient.isConnected()
     */
    public boolean isConnected() {
        return mGoogleApiClient != null && mGoogleApiClient.isConnected();
    }

    /**
     * Initialization of the GoogleApiClient with Callbacks, Listeners and View for popups.
     * @param activity
     * @param view
     */
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
