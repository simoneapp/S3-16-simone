package actors;

import akka.actor.UntypedActor;
import messages.IMessage;

/**
 * Created by sapi9 on 19/06/2017.
 */

public class PlayerActorJava extends UntypedActor {


    @Override
    public void onReceive(Object message) throws Exception {
        switch (((IMessage) message).getType()) {
            case START_GAME_VS_CPU:
                /*ActorRef view = Utilities.getActorByName("/user/view-actor", context().system());
                view.tell(new StartGameVsCPUMsg(((StartGameVsCPUMsg)message).getMode()),getSelf());*/
                break;
        }
    }
}
