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
import app.simone.singleplayer.model.SimonColor;
import app.simone.singleplayer.model.SimonColorImpl;
import app.simone.singleplayer.messages.TimeToBlinkMsg;
import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.messages.StartGameVsCPUMsg;
import app.simone.shared.utils.Constants;
import app.simone.shared.utils.Utilities;

/**
 * CPUActor.
 * Generates a step-by-step sequence, communicates with the GameViewActor.
 * @author Michele Sapignoli
 */
public class CPUActor extends UntypedActor {
    private int nColors = 0;
    private List<SimonColorImpl> currentSequence;
    private List<SimonColorImpl> multiplayerFullSequence;

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
                    this.multiplayerFullSequence.clear();
                }
                Log.d("##CPU ACTOR", "Received StartGameVsCpuMSG, " + this.nColors + " colors.");
                this.generateAndSendColor(Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().getActorSystem()));
                break;
            case GIMME_NEW_COLOR_MSG:
                 /*
                 Computes a new color, adds to sequence and tells the GameViewActor the modified sequence
                 */
                this.generateAndSendColor(getSender());
                break;
            case COMPUTE_FULL_MULTIPLAYER_SEQUENCE_MSG:
                /*
                 1st player in multiplayer classic mode - Computes a full sequence of 100 colors to play in multiplayer classic mode and communicates to the GameViewActor "let the game begin!"
                 */
                for (int i = 0; i <= 100; i++) {
                    this.multiplayerFullSequence.add(SimonColorImpl.values()[new Random().nextInt(((ComputeFullMultiplayerSequenceMsg) message).getNColors())]);
                }
                final String key= ((ComputeFullMultiplayerSequenceMsg) message).getMatchKey();
                DataManager.Companion.getInstance().getDatabase().child(key).child("sequence").setValue(this.multiplayerFullSequence);

                ((ComputeFullMultiplayerSequenceMsg) message).getActivity().getHandler().sendEmptyMessage(Constants.MULTIPLAYER_READY);
                break;
            case RECEIVED_SEQUENCE_MSG:
                /*
                 2nd player in multiplayer classic mode - Receives the sequence and communicates to the GameViewActor "let the game begin!"
                 */
                this.multiplayerFullSequence=((ReceivedSequenceMsg) message).getSequence();
                ((ReceivedSequenceMsg) message).getActivity().getHandler().sendEmptyMessage(Constants.MULTIPLAYER_READY);
                break;
        }
    }


    /**
     * Generate a color and send the updated sequeunce to the GameViewActor.
     * @param viewActor
     */
    private void generateAndSendColor(ActorRef viewActor) {
        /*
        Different behaviour depending on singleplayer or multiplayer
         */
        if(multiplayerFullSequence == null || multiplayerFullSequence.isEmpty()){
            this.currentSequence.add(SimonColorImpl.values()[new Random().nextInt(nColors)]);
        } else {
            this.currentSequence.add(this.multiplayerFullSequence.get(currentSequence.size()));
        }


        Log.d("##CPU ACTOR", "Generated new color in sequence, now sequence is" + this.currentSequence.toString());
        viewActor.tell(new TimeToBlinkMsg(this.currentSequence), getSelf());
    }


}


