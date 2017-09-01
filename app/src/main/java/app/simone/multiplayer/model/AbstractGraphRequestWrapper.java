package app.simone.multiplayer.model;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;

/**
 * Created by nicola on 28/08/2017.
 */

public abstract class AbstractGraphRequestWrapper implements GraphRequestWrapper {

    protected GraphRequest request;

    public AbstractGraphRequestWrapper() {

    }

    public AbstractGraphRequestWrapper(
            AccessToken accessToken,
            String graphPath,
            Bundle parameters,
            HttpMethod httpMethod,
            GraphRequest.Callback callback) {
        this.request = new GraphRequest(accessToken, graphPath, parameters, httpMethod, callback);
    }

    public GraphRequest getRequest() {
        return request;
    }

    public void setRequest(GraphRequest request) {
        this.request = request;
    }
}
