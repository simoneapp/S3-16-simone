package app.simone;

import android.os.Bundle;

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

import static junit.framework.Assert.assertTrue;

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
    public void testCorrectFacebookFriends() {
        new JavaTestKit(system) {{
            JavaTestKit probe = new JavaTestKit(system);

            Bundle bundle = Mockito.mock(Bundle.class);
            MockGraphRequestWrapper request = new MockGraphRequestWrapper(new CorrectMockingStrategy());
            getFbManagerActor().tell(new FbRequestFriendsMsgMock(bundle, request), probe.getRef());

            FbResponseFriendsMsg message = probe.expectMsgClass(duration("30 seconds"), FbResponseFriendsMsg.class);
            assertTrue(message.getData().size() > 0);
            assertTrue(message.getErrorMessage() == null || message.getErrorMessage().equals(""));
        }};
    }

    /*
    @Test
    public void testWrongFacebookFriends() {
        new JavaTestKit(system) {{
            JavaTestKit probe = new JavaTestKit(system);

            Bundle bundle = Mockito.mock(Bundle.class);
            MockGraphRequestWrapper request = new MockGraphRequestWrapper(new WrongMockingStrategy());
            getFbManagerActor().tell(new FbRequestFriendsMsgMock(bundle, request), probe.getRef());

            FbResponseFriendsMsg message = probe.expectMsgClass(duration("30 seconds"), FbResponseFriendsMsg.class);
            assertTrue(message.getData() == null);
            assertTrue(message.getErrorMessage() != null || !message.getErrorMessage().equals(""));
        }};
    }
*/
    
    private ActorRef getFbManagerActor() {
        return Utilities.getActorByName(Constants.PATH_ACTOR + Constants.FACEBOOK_ACTOR_NAME, system);
    }
}
