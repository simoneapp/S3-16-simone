package app.simone.singleplayer.messages;

import java.util.List;

import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.model.SColor;
import app.simone.singleplayer.view.IGameActivity;

/**
 * ReceivedSequenceMsg,
 * containing the multiplayer sequence to play.
 * @author Michele Sapignoli
 */
public class ReceivedSequenceMsg implements IMessage {
    private List<SColor> sequence;
    private IGameActivity activity;


    public ReceivedSequenceMsg(List<SColor> listToPlay,IGameActivity activity){
        this.sequence = listToPlay;
        this.activity = activity;
    }

    @Override
    public MessageType getType() {
        return MessageType.RECEIVED_SEQUENCE_MSG;
    }

    public List<SColor> getSequence() {
        return sequence;
    }

    public IGameActivity getActivity() {
        return activity;
    }
}
