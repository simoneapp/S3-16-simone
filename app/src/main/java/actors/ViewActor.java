package actors;

import android.util.Log;

import Model.interfaces.IActivity;
import akka.actor.UntypedActor;
import messages.AttachViewMsg;
import messages.IMessage;

/**
 * Created by sapi9 on 19/06/2017.
 */

public class ViewActor extends UntypedActor{
    private IActivity gameActivity;

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        switch (((IMessage) message).getType()) {
            case ATTACH_VIEW_MSG:
                this.gameActivity = ((AttachViewMsg)message).getIActivity();
                Log.d("VIEW ACTOR", "Current GameActivity registered");
                break;
            case BLINK_MSG:

                break;
        }
    }


}
