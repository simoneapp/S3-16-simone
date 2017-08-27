package app.simone;

import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;

/**
 * Created by nicola on 28/08/2017.
 */

public class GraphRequestMock extends GraphRequest {


    public GraphRequestAsyncTask executeAsinc() {
        return GraphRequest.executeBatchAsync(this);
    }

}
