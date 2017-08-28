package app.simone;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.mockito.Mockito;

import app.simone.multiplayer.model.AbstractGraphRequestWrapper;

/**
 * Created by nicola on 28/08/2017.
 */

public class MockGraphRequestWrapper extends AbstractGraphRequestWrapper {

    private MockingStrategy strategy;

    public MockGraphRequestWrapper(AccessToken accessToken,
                                   String graphPath,
                                   Bundle parameters,
                                   HttpMethod httpMethod,
                                   GraphRequest.Callback callback) {
        super(accessToken, graphPath, parameters, httpMethod, callback);
    }

    public MockGraphRequestWrapper(MockingStrategy strategy) {
        super(null, null, null, null, null);
        this.strategy = strategy;
    }

    @Override
    public void executeAsync() {
        GraphResponse response = Mockito.mock(GraphResponse.class);
        Mockito.when(response.getRawResponse()).thenReturn(strategy.getRawContent());
/*
        FacebookRequestError error = null;
        if(!strategy.getSuccess()) {
            error = new Error();
        }

        Mockito.when(response.getError()).thenReturn(error);*/
        this.request.getCallback().onCompleted(response);
    }
}
