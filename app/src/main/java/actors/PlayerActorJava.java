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
import messages.StartGameVsCPUMsg;
import scala.concurrent.Await;
import scala.concurrent.Future;
import utils.Utilities;

/**
 * Created by sapi9 on 19/06/2017.
 */

public class PlayerActorJava extends UntypedActor {


    @Override
    public void onReceive(Object message) throws Exception {
        switch (((IMessage) message).getType()) {
            case STARTGAMEVSCPU:
                ActorRef view = Utilities.getActorByName("/user/view-actor", context());
                view.tell(new StartGameVsCPUMsg(((StartGameVsCPUMsg)message).getnColors()),getSelf());
                break;
        }
    }
}
