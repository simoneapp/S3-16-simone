package app.simone.singleplayer.controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import app.simone.multiplayer.controller.DataManager;
import app.simone.shared.application.App;
import app.simone.singleplayer.messages.ComputeFullMultiplayerSequenceMsg;
import app.simone.singleplayer.messages.ReceivedSequenceMsg;
import app.simone.singleplayer.model.SColor;
import app.simone.singleplayer.messages.TimeToBlinkMsg;
import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.messages.StartGameVsCPUMsg;
import app.simone.shared.utils.Constants;
import app.simone.shared.utils.Utilities;

/**
 * @author Michele Sapignoli
 */

public class CPUActor extends UntypedActor {
    private int nColors = 0;
    private List<SColor> currentSequence;
    private List<SColor> multiplayerFullSequence;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.currentSequence = new ArrayList<>();
        this.multiplayerFullSequence = new ArrayList<>();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        switch (((IMessage) message).getType()) {
            case START_GAME_VS_CPU:
                /*
                Received StartGameVsCPUMsg from GameViewActor Actor TimeToBlinkMsg to GameViewActor
                 */
                this.nColors = ((StartGameVsCPUMsg) message).getnColors();
                this.currentSequence.clear();
                if(((StartGameVsCPUMsg) message).isSinglePlay()){
                    this.multiplayerFullSequence = null;git
                }
                Log.d("##CPU ACTOR", "Received StartGameVsCpuMSG, " + this.nColors + " colors.");
                this.generateAndSendColor(Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().getActorSystem()));
                break;
            case GIMME_NEW_COLOR_MSG:
                this.generateAndSendColor(getSender());
                break;
            case COMPUTE_FULL_MULTIPLAYER_SEQUENCE_MSG:
                for (int i = 0; i <= 100; i++) {
                    this.multiplayerFullSequence.add(SColor.values()[new Random().nextInt(((ComputeFullMultiplayerSequenceMsg) message).getNColors())]);
                }


                //if second and first

                final String key= ((ComputeFullMultiplayerSequenceMsg) message).getMatchKey();
                DataManager.Companion.getInstance().getDatabase().child(key).child("sequence").setValue(this.multiplayerFullSequence);

                ((ComputeFullMultiplayerSequenceMsg) message).getActivity().getHandler().sendEmptyMessage(Constants.MULTIPLAYER_READY);
                break;
            case RECEIVED_SEQUENCE_MSG:
                this.multiplayerFullSequence=((ReceivedSequenceMsg) message).getSequence();
                ((ReceivedSequenceMsg) message).getActivity().getHandler().sendEmptyMessage(Constants.MULTIPLAYER_READY);
                break;
        }
    }



    private void generateAndSendColor(ActorRef viewActor) {
        if(multiplayerFullSequence==null){
            this.currentSequence.add(SColor.values()[new Random().nextInt(nColors)]);
        } else {
            this.currentSequence.add(this.multiplayerFullSequence.get(currentSequence.size()));
        }


        Log.d("##CPU ACTOR", "Generated new color in sequence, now sequence is" + this.currentSequence.toString());
        viewActor.tell(new TimeToBlinkMsg(this.currentSequence), getSelf());
    }


}


