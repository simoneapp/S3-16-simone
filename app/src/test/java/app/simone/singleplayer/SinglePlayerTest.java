package app.simone.singleplayer;

import org.junit.Test;

import akka.actor.ActorRef;
import app.simone.ActorTests;
import app.simone.shared.utils.Constants;
import app.simone.singleplayer.controller.GameActivityPresenter;
import app.simone.singleplayer.view.GameActivity;

import static org.mockito.Mockito.mock;

/**
 * Created by nicola on 29/08/2017.
 */

public class SinglePlayerTest extends ActorTests {

    @Test
    public void testPresenterCreation() {/*
        new JavaTestKit(system) {{
            GameActivityPresenter sent = buildPresenter(getTestActor());
            GameActivityPresenter received = expectMsgClass(defaultDuration, GameActivityPresenter.class);
            //If the received presenter is the same, it means that is correctly set in the Actor.
            assertTrue(sent == received);
        }};*/
    }


    @Test
    public void testStartGame() {/*
        new JavaTestKit(system) {{
            GameActivityPresenter presenter = buildPresenter(getTestActor());
            expectMsgClass(defaultDuration, GameActivityPresenter.class);
            presenter.prepareGame(new StartGameVsCPUMsg(true, system));
            assertFalse(presenter.isTapToBegin());
            assertFalse(presenter.isPlayerBlinking());
            assertTrue(presenter.getFinalScore() == 0);
            StartGameVsCPUMsg startGameMsg = expectMsgClass(defaultDuration, StartGameVsCPUMsg.class);
            assertTrue(startGameMsg != null);
            NextColorMsg nextColorMsg = expectMsgClass(defaultDuration, NextColorMsg.class);
            assertTrue(nextColorMsg != null);

        }};*/
    }


    private GameActivityPresenter buildPresenter(ActorRef testActor) {
        GameActivity activity = mock(GameActivity.class);
        return new GameActivityPresenter(activity, Constants.CLASSIC_MODE, system, testActor);
    }


}
