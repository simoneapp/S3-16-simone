package app.simone.multiplayer;

import android.os.Bundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import akka.actor.ActorRef;
import akka.testkit.JavaTestKit;
import app.simone.ActorTests;
import app.simone.multiplayer.messages.FbRequestFriendsMsgMock;
import app.simone.multiplayer.messages.FbResponseFriendsMsg;
import app.simone.shared.utils.Constants;
import app.simone.shared.utils.Utilities;

import static junit.framework.Assert.assertTrue;

/**
 * Created by nicola on 28/08/2017.
 */

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
public class FacebookManagerTest extends ActorTests {

    @Test
    public void testCorrectFacebookFriends() {
        new JavaTestKit(system) {{
            JavaTestKit probe = new JavaTestKit(system);

            Bundle bundle = Mockito.mock(Bundle.class);
            MockGraphRequestWrapper request = new MockGraphRequestWrapper(new CorrectMockingStrategy());
            getFbManagerActor().tell(new FbRequestFriendsMsgMock(bundle, request), probe.getRef());

            FbResponseFriendsMsg message = probe.expectMsgClass(defaultDuration, FbResponseFriendsMsg.class);
            assertTrue(message.getData().size() > 0);
            assertTrue(message.getErrorMessage() == null || message.getErrorMessage().equals(""));
        }};
    }

    private ActorRef getFbManagerActor() {
        return Utilities.getActor(Constants.FACEBOOK_ACTOR_NAME, system);
    }
}
