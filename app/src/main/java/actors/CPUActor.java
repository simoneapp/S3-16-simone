package actors;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import akka.actor.UntypedActor;
import colors.Color;
import messages.TimeToBlinkMsg;
import messages.IMessage;
import messages.StartGameVsCPUMsg;

/**
 * @author Michele Sapignoli
 */

public class CPUActor extends UntypedActor {
    private int nColors = 0;
    private List<Color> currentSequence;

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
                Log.d("##CPU ACTOR", "Received StartGameVsCpuMSG, " + this.nColors +" colors.");
                this.generateAndSendColor();
                break;
            case GIMME_NEW_COLOR_MSG:
                this.generateAndSendColor();
                break;
        }
    }

    private void generateAndSendColor(){
        this.currentSequence.add(Color.values()[new Random().nextInt(nColors)]);
        Log.d("##CPU ACTOR", "Generated new color in sequence, now sequence is" + this.currentSequence.toString());
        getSender().tell(new TimeToBlinkMsg(this.currentSequence),getSelf());
    }

}


