package PubNub;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.models.consumer.PNStatus;

import java.util.Arrays;

/**
 * Created by Giacomo on 01/07/2017.
 */

public class PresenceController {

    PubNub pubnub;

    public PresenceController() {

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-85c112e6-5b17-11e7-b679-0619f8945a4f");
        pnConfiguration.setPublishKey("pub-c-c4780444-fc34-418a-83be-9072a54a2d0d");
        pnConfiguration.setUuid("Giak");
        pubnub = new PubNub(pnConfiguration);

    }

    public void onlinePresence(){
        pubnub.subscribe()
                .channels(Arrays.asList("multiplayer_channel")) // subscribe to channels
                .withPresence() // also subscribe to related presence information
                .execute();
    }

}
