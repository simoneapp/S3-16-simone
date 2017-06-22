package actors;

import android.util.Log;

import java.util.List;

import app.simone.IGameActivity;
import akka.actor.UntypedActor;
import application.mApplication;
import messages.AttachViewMsg;
import messages.BlinkMsg;
import messages.IMessage;
import messages.StartGameVsCPUMsg;
import messages.YourTurnMsg;
import utils.Constants;
import utils.Utilities;

/**
 * @author Michele Sapignoli
 */

public class GameViewActor extends UntypedActor{
    private IGameActivity gameActivity;

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        switch (((IMessage) message).getType()) {
            case ATTACH_VIEW_MSG:
                this.gameActivity = ((AttachViewMsg)message).getIActivity();

                /*
                StartGameVsCPUMsg to CPU Actor
                 */
                Utilities.getActorByName(Constants.PATH_ACTOR + Constants.CPU_ACTOR_NAME, mApplication.getActorSystem())
                        .tell(new StartGameVsCPUMsg(((AttachViewMsg)message).getRadiobtnIndex()), getSelf());
                Log.d("##VIEW ACTOR", "Current GameActivity registered + StartGameVSCPUMsg sent to CPUActor ACTOR");
                break;
            case BLINK_MSG:
                List sequence = ((BlinkMsg)message).getSequence();
                Log.d("##VIEW ACTOR", "Received BlinkMsg from CPUActor, colors to blink:" + sequence.toString());
                this.blink(sequence);
                /*
                YourTurnMsg to Player Actor
                 */
                Utilities.getActorByName(Constants.PATH_ACTOR + Constants.PLAYER_ACTOR_NAME, mApplication.getActorSystem())
                        .tell(new YourTurnMsg(sequence), getSelf());
                break;
        }
    }

    private void blink(List<Integer> sequence){
        for (int color: sequence) {
            this.gameActivity.getViewHandler().sendEmptyMessage(color);
        }
    }

}
