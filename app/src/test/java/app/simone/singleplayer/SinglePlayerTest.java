package app.simone.singleplayer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.testkit.JavaTestKit;
import app.simone.ActorTests;
import app.simone.shared.utils.Constants;
import app.simone.singleplayer.controller.GameActivityPresenter;
import app.simone.singleplayer.messages.AttachPresenterMsg;
import app.simone.singleplayer.messages.MessageType;
import app.simone.singleplayer.messages.PauseMsg;
import app.simone.singleplayer.messages.PlayerTurnMsg;
import app.simone.singleplayer.messages.StartGameVsCPUMsg;
import app.simone.singleplayer.messages.TestMessage;
import app.simone.singleplayer.messages.TimeToBlinkMsg;
import app.simone.singleplayer.model.SimonColorImpl;
import app.simone.singleplayer.view.GameActivity;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Created by nicola on 29/08/2017.
 */

public class SinglePlayerTest extends ActorTests {

    SinglePlayerActorsContainer ac = new SinglePlayerActorsContainer(system);

    @Test
    public void testPresenterCreation() throws Exception {

        new JavaTestKit(system) {{
            GameActivityPresenter presenter = buildPresenter(ac.getGameViewActorRef());
            Future<Object> future = Patterns.ask(ac.getGameViewActorRef(), new AttachPresenterMsg(presenter), 3000);
            Await.ready(future, Duration.Inf());

            assertTrue(future.isCompleted());
            assertTrue(presenter == ac.getGameViewActor().getPresenter());
        }};
    }


    @Test
    public void testStartGame() throws Exception {

        new JavaTestKit(system) {{

            StartGameVsCPUMsg msg = new StartGameVsCPUMsg(true, ac.getGameViewActorRef());
            Future<Object> future = Patterns.ask(ac.getCpuActorRef(), msg, 3000);
            assertTrue(future.isCompleted());

            TestMessage received = (TestMessage)Await.result(future, Duration.Zero());
            assertEquals(MessageType.TEST, received.getType());

            assertTrue(ac.getCpuActor().getCurrentSequence().size() == 1);
            assertTrue(ac.getCpuActor().getnColors() == Constants.CLASSIC_MODE);
        }};
    }

    @Test
    public void testTimeToBlink() throws Exception {

        new JavaTestKit(system) {{

            List<SimonColorImpl> colors = getExampleSequence();
            TimeToBlinkMsg msg = new TimeToBlinkMsg(colors);
            Future<Object> future = Patterns.ask(ac.getGameViewActorRef(), msg, 3000);
            assertTrue(future.isCompleted());

            TestMessage received = (TestMessage)Await.result(future, Duration.Zero());
            assertEquals(MessageType.TEST, received.getType());

            assertTrue(ac.getGameViewActor().getCpuColorIndex() == 0);
            assertFalse(ac.getGameViewActor().isPaused());
            assertFalse(ac.getGameViewActor().isPlayerTurn());
            assertTrue(ac.getGameViewActor().getCpuSequence().equals(colors));

        }};
    }

    @Test
    public void testHandlePlayerMsg() throws Exception {

        new JavaTestKit(system) {{

            PlayerTurnMsg msg = new PlayerTurnMsg();
            Future<Object> future = Patterns.ask(ac.getGameViewActorRef(), msg, 3000);
            assertTrue(future.isCompleted());

            TestMessage received = (TestMessage)Await.result(future, Duration.Zero());
            assertEquals(MessageType.TEST, received.getType());

            assertEquals(ac.getGameViewActor().getPlayerColorIndex(), 0);
            assertEquals(ac.getGameViewActor().getPlayerSequence().size(), 0);
        }};
    }

    @Test
    public void testPause() throws Exception {

        new JavaTestKit(system) {{

            PauseMsg pause1 = new PauseMsg(true);
            Future<Object> future1 = Patterns.ask(ac.getGameViewActorRef(), pause1, 3000);
            assertTrue(future1.isCompleted());
            Await.result(future1, Duration.Zero());

            assertTrue(ac.getGameViewActor().isPaused());

            PauseMsg pause2 = new PauseMsg(false);
            Future<Object> future2 = Patterns.ask(ac.getGameViewActorRef(), pause2, 3000);
            assertTrue(future2.isCompleted());
            Await.result(future2, Duration.Zero());

            assertFalse(ac.getGameViewActor().isPaused());

            TestMessage received3 = (TestMessage)Await.result(future2, Duration.Zero());
            assertEquals(MessageType.TEST, received3.getType());
            assertFalse(ac.getGameViewActor().isPaused());
        }};
    }

    @Test
    public void testNextColor() throws Exception {

        new JavaTestKit(system) {{

            List<SimonColorImpl> colors = getExampleSequence();
            TimeToBlinkMsg msg = new TimeToBlinkMsg(colors);
            Future<Object> future = Patterns.ask(ac.getGameViewActorRef(), msg, 3000);
            assertTrue(future.isCompleted());
            Await.result(future, Duration.Zero());
            assertTrue(future.isCompleted());
            Await.result(future, Duration.Zero());
            assertFalse(ac.getGameViewActor().isPlayerTurn());
        }};
    }

    /*
    @Test
    public void testGuessColor() throws Exception {

        new JavaTestKit(system) {{

            ActorsContainer ac = new ActorsContainer(system);

            List<SimonColorImpl> colors = getExampleSequence();
            TimeToBlinkMsg msg1 = new TimeToBlinkMsg(colors);
            Future<Object> future1 = Patterns.ask(ac.getGameViewActorRef(), msg1, 30000);
            assertTrue(future1.isCompleted());
            Await.result(future1, Duration.Zero());

            NextColorMsg msg2 = new NextColorMsg();
            Future<Object> future2 = Patterns.ask(ac.getGameViewActorRef(), msg2, 30000);
            assertTrue(future2.isCompleted());
            Await.result(future2, Duration.Zero());

            GuessColorMsg msg3 = new GuessColorMsg(colors.get(0));
            Future<Object> future3 = Patterns.ask(ac.getGameViewActorRef(), msg3, 30000);
            assertTrue(future3.isCompleted());
            Await.result(future3, Duration.Zero());

            }};

    }*/


    private List<SimonColorImpl> getExampleSequence() {

        List<SimonColorImpl> colors = new ArrayList<SimonColorImpl>();
        colors.add(SimonColorImpl.BLUE);
        colors.add(SimonColorImpl.RED);
        colors.add(SimonColorImpl.YELLOW);
        colors.add(SimonColorImpl.GREEN);

        return colors;
    }


    private GameActivityPresenter buildPresenter(ActorRef testActor) {
        GameActivity activity = mock(GameActivity.class);
        return new GameActivityPresenter(activity, Constants.CLASSIC_MODE, system, testActor);
    }


}
