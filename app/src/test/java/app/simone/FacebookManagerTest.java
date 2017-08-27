package app.simone;

import android.os.Bundle;

import com.facebook.GraphRequest;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import app.simone.multiplayer.messages.FbRequestFriendsMsgMock;
import app.simone.multiplayer.messages.FbResponseFriendsMsg;
import app.simone.shared.application.App;
import app.simone.shared.utils.Constants;
import app.simone.shared.utils.Utilities;

/**
 * Created by nicola on 28/08/2017.
 */

public class FacebookManagerTest {

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = App.buildActorSystem();
    }

    @Test
    public void testFbFriends() {

        new JavaTestKit(system) {{
            JavaTestKit probe = new JavaTestKit(system);
            ActorRef fbManager = getFbManagerActor();

            Bundle bundle = Mockito.mock(Bundle.class);
            GraphRequest request = Mockito.mock(GraphRequest.class);
            Mockito.when(request.executeAsync());

            fbManager.tell(new FbRequestFriendsMsgMock(bundle, request), probe.getRef());
            probe.expectMsgClass(duration("5 seconds"), FbResponseFriendsMsg.class);
        }};
    }

    private ActorRef getFbManagerActor() {
        return Utilities.getActorByName(Constants.PATH_ACTOR + Constants.FACEBOOK_ACTOR_NAME, system);
    }
}
