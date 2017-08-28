package app.simone.singleplayer.controller;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import app.simone.multiplayer.controller.DataManager;
import app.simone.shared.application.App;
import app.simone.shared.messages.IMessage;
import app.simone.shared.utils.Constants;
import app.simone.shared.utils.Utilities;
import app.simone.singleplayer.messages.ComputeFullMultiplayerSequenceMsg;
import app.simone.singleplayer.messages.ReceivedSequenceMsg;
import app.simone.singleplayer.messages.StartGameVsCPUMsg;
import app.simone.singleplayer.messages.TimeToBlinkMsg;
import app.simone.singleplayer.model.SimonColorImpl;

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

    /**
     * Entry point for received messages.
     * @param message
     * @throws Exception
     */
    @Override
    public void onReceive(Object message) throws Exception {
        switch (((IMessage) message).getType()) {
            case START_GAME_VS_CPU:
                startGame((StartGameVsCPUMsg)message);
                break;
            case GIMME_NEW_COLOR_MSG:
                generateAndSendColor(getSender());
                break;
            case COMPUTE_FULL_MULTIPLAYER_SEQUENCE_MSG:
                computeMultiplayerSequence((ComputeFullMultiplayerSequenceMsg)message);
                break;
            case RECEIVED_SEQUENCE_MSG:
                handleReceivedSequence((ReceivedSequenceMsg)message);
                break;
        }
    }

    /**
     * Received StartGameVsCPUMsg from GameViewActor Actor TimeToBlinkMsg to GameViewActor
     * @param message
     */
    private void startGame(StartGameVsCPUMsg message) {
        this.nColors = message.getnColors();
        this.currentSequence.clear();
        if(message.isSinglePlay()){
            this.multiplayerFullSequence.clear();
        }

        ActorRef actor = Utilities.getActor(Constants.GAMEVIEW_ACTOR_NAME,
                App.getInstance().getActorSystem());
        this.generateAndSendColor(actor);
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

        viewActor.tell(new TimeToBlinkMsg(this.currentSequence), getSelf());
    }

    /**
     *  1st player in multiplayer classic mode - Computes a full sequence of 100 colors to play in
     *  multiplayer classic mode and communicates to the GameViewActor "let the game begin!"
     *  @param message
     */
    private void computeMultiplayerSequence(ComputeFullMultiplayerSequenceMsg message) {

        for (int i = 0; i <= 100; i++) {
            int random = new Random().nextInt(message.getNColors());
            this.multiplayerFullSequence.add(SimonColorImpl.values()[random]);
        }

        final String key = message.getMatchKey();
        DatabaseReference ref = DataManager.Companion.getInstance().getDatabase();
        ref.child(key).child("sequence").setValue(this.multiplayerFullSequence);
        message.getPresenter().getHandler().sendEmptyMessage(Constants.MULTIPLAYER_READY);
    }

    /**
     * 2nd player in multiplayer classic mode - Receives the sequence and communicates to the
     * GameViewActor "let the game begin!"
     * @param message
     */
    private void handleReceivedSequence(ReceivedSequenceMsg message) {
        this.multiplayerFullSequence = message.getSequence();
        message.getPresenter().getHandler().sendEmptyMessage(Constants.MULTIPLAYER_READY);
    }

}


