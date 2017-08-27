package app.simone;

import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import app.simone.shared.application.App;
import app.simone.shared.utils.Constants;
import app.simone.shared.utils.Utilities;

/**
 * Created by nicola on 27/08/2017.
 */
@RunWith(AndroidJUnit4.class)
public class MultiplayerControllerTest {


    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = App.buildActorSystem();
    }
/*
    @Test
    public void checkActorsCreation() {

        new TestKit(system) {{

            MultiplayerPagerActivity mock = Mockito.mock(MultiplayerPagerActivity.class);

            getFbManagerActor().tell(new FbRequestFriendsMsg(mock), testActor());

            assertTrue(true);


            }};
    }
*/
    private ActorRef getFbViewActor() {
        return Utilities.getActorByName(Constants.PATH_ACTOR + Constants.FBVIEW_ACTOR_NAME, system);
    }

    private ActorRef getFbManagerActor() {
        return Utilities.getActorByName(Constants.PATH_ACTOR + Constants.FACEBOOK_ACTOR_NAME, system);
    }

}

