package actors;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import application.mApplication;
import colors.SColor;
import messages.TimeToBlinkMsg;
import messages.IMessage;
import messages.StartGameVsCPUMsg;
import utils.Constants;
import utils.Utilities;

/**
 * @author Michele Sapignoli
 */

public class CPUActor extends UntypedActor {
    private int nColors = 0;
    private List<SColor> currentSequence;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.currentSequence = new ArrayList<>();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        switch (((IMessage) message).getType()) {
            case START_GAME_VS_CPU:
                /*
                Received StartGameVsCPUMsg from GameViewActor Actor TimeToBlinkMsg to GameViewActor
                 */
                this.nColors = ((StartGameVsCPUMsg)message).getnColors();
                this.currentSequence.clear();
                Log.d("##CPU ACTOR", "Received StartGameVsCpuMSG, " + this.nColors +" colors.");
                this.generateAndSendColor(Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, mApplication.getActorSystem()));
                break;
            case GIMME_NEW_COLOR_MSG:
                this.generateAndSendColor(getSender());
                break;
        }
    }

    private void generateAndSendColor(ActorRef viewActor){
        this.currentSequence.add(SColor.values()[new Random().nextInt(nColors)]);
        Log.d("##CPU ACTOR", "Generated new color in sequence, now sequence is" + this.currentSequence.toString());
        viewActor.tell(new TimeToBlinkMsg(this.currentSequence),getSelf());
    }

}


