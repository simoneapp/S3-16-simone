package actors;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import akka.actor.UntypedActor;
import messages.BlinkMsg;
import messages.IMessage;
import messages.StartGameVsCPUMsg;
import utils.Constants;
import utils.Utilities;

/**
 * @author Michele Sapignoli
 */

public class CPUActor extends UntypedActor {
    private int nColors = 0;
    private List<Integer> currentSequence;

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
                Received StartGameVsCPUMsg from GameViewActor Actor BlinkMsg to GameViewActor
                 */
                this.nColors = ((StartGameVsCPUMsg)message).getnColors();
                Log.d("##CPU ACTOR", "Received StartGameVsCpuMSG, " + this.nColors +" colors.");
                this.generateColor();
                getSender().tell(new BlinkMsg(this.currentSequence),getSelf());
                break;
            case NEXT_COLOR_MSG:
                this.generateColor();
                getSender().tell(new BlinkMsg(this.currentSequence),getSelf());
                break;
        }
    }

    private void generateColor(){
        this.currentSequence.add(new Random().nextInt(nColors));
        Log.d("##CPU ACTOR", "Generated new color in sequence, now sequence is" + this.currentSequence.toString());
    }

}


