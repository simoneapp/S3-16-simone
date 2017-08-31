package app.simone.multiplayer.model;

import com.facebook.GraphRequest;

/**
 * Created by nicola on 28/08/2017.
 */

public interface GraphRequestWrapper {
    void executeAsync();
    GraphRequest getRequest();
    void setRequest(GraphRequest request);
}
