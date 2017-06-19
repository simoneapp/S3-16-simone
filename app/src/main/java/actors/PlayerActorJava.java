package actors;

import java.util.concurrent.TimeUnit;

import Model.GameMessage;
import akka.actor.Actor;
import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Identify;
import akka.actor.UntypedActor;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;
import messages.IMessage;
import scala.concurrent.Await;
import scala.concurrent.Future;

/**
 * Created by sapi9 on 19/06/2017.
 */

public class PlayerActorJava extends UntypedActor {


    @Override
    public void onReceive(Object message) throws Exception {
        switch (((IMessage) message).getType()) {
            case STARTGAMEVSCPU:
                ActorSelection sel = context().actorSelection("/user/view-actor");

                Timeout t = new Timeout(3, TimeUnit.SECONDS);
                AskableActorSelection asker = new AskableActorSelection(sel);
                Future<Object> fut = asker.ask(new Identify(1), t);
                ActorIdentity ident = (ActorIdentity) Await.result(fut, t.duration());
                ActorRef ref = ident.getRef();
                ref.tell(new GameMessage("lol"),getSelf());
                break;
        }
    }
}
