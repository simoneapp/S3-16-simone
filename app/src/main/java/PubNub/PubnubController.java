package PubNub;


import android.util.Log;
import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import org.json.JSONException;
import java.util.Arrays;

import static com.github.salomonbrys.kotson.ElementKt.toMap;

/**
 * Created by Giacomo on 29/06/2017.
 */

public class PubnubController {

    private PubNub pubnub;
    private String channel;

    public PubnubController(String channel) {
        configPubnub();
        this.channel=channel;
    }


    private void configPubnub(){
        //initial configuration
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-85c112e6-5b17-11e7-b679-0619f8945a4f");
        pnConfiguration.setPublishKey("pub-c-c4780444-fc34-418a-83be-9072a54a2d0d");
        pnConfiguration.setSecure(false);

        // 1 - Creating a PubNub object
        pubnub = new PubNub(pnConfiguration);
        Log.d("GIAK","PUBNUB CONFIGURATO");
    }


    public void subscribeToChannel(){
        // 2 - Subscribe to a channel
        pubnub.subscribe()
                .channels(Arrays.asList(this.channel)) // subscribe to channels
                .execute();
    }


    public void publishToChannel(Request request) throws JSONException {
        //3 - Publish to a channel

        /* Taken from the documentation:
        *  The Android V4 client SDK, like many of the PubNub SDKs, is asynchronous -- publish() can,
        *  and most likely will, fire before the previously executed subscribe() call completes.
        *  The result is, for a single-client instance, you would never receive (via subscribing)
        *  the message you just published, because the subscribe operation did not complete before the message was published.
        * */


        JsonObject obj = new JsonObject();
        obj.addProperty("from", request.getSender().getId());
        obj.addProperty("fromName",request.getSender().getName());
        obj.addProperty("to", request.getRecipient().getId());
        obj.addProperty("toName",request.getRecipient().getName());


        pubnub.publish()
                .message(toMap(obj))
                .channel(this.channel)
                .shouldStore(true)
                .usePOST(true)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        // handle publish result, status always present, result if successful
                        // status.isError to see if error happened
                        if(!status.isError()) {
                            System.out.println("pub timetoken: " + result.getTimetoken());
                        }
                        System.out.println("pub status code: " + status.getStatusCode());
                    }
                });
    }


    public void unscribe(){
        pubnub.unsubscribe()
                .channels(Arrays.asList(this.channel))
                .execute();
    }

    public PubNub getPubnub(){return this.pubnub;}

}
