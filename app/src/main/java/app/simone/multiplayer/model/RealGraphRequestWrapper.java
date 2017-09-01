package app.simone.multiplayer.model;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;

/**
 * Created by nicola on 28/08/2017.
 */

public class RealGraphRequestWrapper extends AbstractGraphRequestWrapper {

    public RealGraphRequestWrapper(AccessToken accessToken,
                                   String graphPath,
                                   Bundle parameters,
                                   HttpMethod httpMethod,
                                   GraphRequest.Callback callback) {
        super(accessToken, graphPath, parameters, httpMethod, callback);
    }

    @Override
    public void executeAsync() {
        this.request.executeAsync();
    }
}
