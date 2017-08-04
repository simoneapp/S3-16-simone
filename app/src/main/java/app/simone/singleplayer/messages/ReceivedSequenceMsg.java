package app.simone.singleplayer.messages;

import java.util.List;

import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.model.SColor;
import app.simone.singleplayer.view.IGameActivity;

/**
 * Created by sapi9 on 27/06/2017.
 */

public class ReceivedSequenceMsg implements IMessage {
    private List<SColor> sequence;
    private IGameActivity activity;


    public ReceivedSequenceMsg(List<SColor> listToPlay,IGameActivity activity){
        this.sequence = listToPlay;
        this.activity =activity;
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
