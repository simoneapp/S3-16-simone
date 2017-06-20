package actors;

import android.util.Log;
import akka.actor.UntypedActor;
import messages.IMessage;
import messages.StartGameVsCPUMsg;

/**
 * Created by sapi9 on 19/06/2017.
 */

public class ViewActor extends UntypedActor{
    @Override
    public void onReceive(Object message) throws Exception {
        switch (((IMessage) message).getType()) {
            case STARTGAMEVSCPU:
                Log.d("VIEWACTOR",""+((StartGameVsCPUMsg)message).getnColors());
                break;
        }
    }
}
