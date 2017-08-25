package app.simone.singleplayer.messages;

import java.util.List;

import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.model.SimonColor;
import app.simone.singleplayer.model.SimonColorImpl;
import app.simone.singleplayer.view.GameActivity;

/**
 * ReceivedSequenceMsg,
 * containing the multiplayer sequence to play.
 * @author Michele Sapignoli
 */
public class ReceivedSequenceMsg implements IMessage {
    private List<SimonColorImpl> sequence;
    private GameActivity activity;


    public ReceivedSequenceMsg(List<SimonColorImpl> listToPlay, GameActivity activity){
        this.sequence = listToPlay;
        this.activity = activity;
    }

    @Override
    public MessageType getType() {
        return MessageType.RECEIVED_SEQUENCE_MSG;
    }

    public List<SimonColorImpl> getSequence() {
        return sequence;
    }

    public GameActivity getActivity() {
        return activity;
    }
}
