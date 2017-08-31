package app.simone.multiplayer;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import app.simone.multiplayer.controller.FacebookManagerActor;
import app.simone.multiplayer.controller.FacebookViewActor;

/**
 * Created by nicola on 31/08/2017.
 */

public class MultiPlayerActorsContainer {

    private TestActorRef<FacebookViewActor> viewActorRef;
    private TestActorRef<FacebookManagerActor> managerActorRef;

    private FacebookViewActor viewActor;
    private FacebookManagerActor managerActor;

    public MultiPlayerActorsContainer(ActorSystem system) {

        Props p1 = Props.create(FacebookViewActor.class);
        viewActorRef = TestActorRef.create(system, p1);
        viewActor = viewActorRef.underlyingActor();

        Props p2 = Props.create(FacebookManagerActor.class);
        managerActorRef = TestActorRef.create(system, p2);
        managerActor = managerActorRef.underlyingActor();
    }

    public TestActorRef<FacebookViewActor> getViewActorRef() {
        return viewActorRef;
    }

    public TestActorRef<FacebookManagerActor> getManagerActorRef() {
        return managerActorRef;
    }

    public FacebookViewActor getViewActor() {
        return viewActor;
    }

    public FacebookManagerActor getManagerActor() {
        return managerActor;
    }
}
