package app.simone.singleplayer;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import app.simone.singleplayer.controller.CPUActor;
import app.simone.singleplayer.controller.GameViewActor;

/**
 * Created by nicola on 30/08/2017.
 */

public class SinglePlayerActorsContainer {

    private TestActorRef<CPUActor> cpuActorRef;
    private TestActorRef<GameViewActor> gameViewActorRef;

    private CPUActor cpuActor;
    private GameViewActor gameViewActor;

    public SinglePlayerActorsContainer(ActorSystem system) {

        Props p1 = Props.create(CPUActor.class);
        cpuActorRef = TestActorRef.create(system, p1);
        cpuActor = cpuActorRef.underlyingActor();

        Props p2 = Props.create(GameViewActor.class);
        gameViewActorRef = TestActorRef.create(system, p2);
        gameViewActor = gameViewActorRef.underlyingActor();
    }

    public CPUActor getCpuActor() {
        return cpuActor;
    }

    public TestActorRef<CPUActor> getCpuActorRef() {
        return cpuActorRef;
    }

    public GameViewActor getGameViewActor() {
        return gameViewActor;
    }

    public TestActorRef<GameViewActor> getGameViewActorRef() {
        return gameViewActorRef;
    }
}
