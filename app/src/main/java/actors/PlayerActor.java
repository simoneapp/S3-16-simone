package actors;

import akka.actor.UntypedActor;
import messages.IMessage;

/**
 * @author Michele Sapignoli
 */

public class PlayerActor extends UntypedActor {


    @Override
    public void onReceive(Object message) throws Exception {
        switch (((IMessage) message).getType()) {
            case YOUR_TURN_MSG:
                
                break;
        }
    }
}
