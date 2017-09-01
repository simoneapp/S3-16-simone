package app.simone.singleplayer.messages;

import java.util.List;

import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.controller.GameActivityPresenter;
import app.simone.singleplayer.model.SimonColorImpl;

/**
 * ReceivedSequenceMsg,
 * containing the multiplayer sequence to play.
 * @author Michele Sapignoli
 */
public class ReceivedSequenceMsg implements IMessage {
    private List<SimonColorImpl> sequence;
    private GameActivityPresenter presenter;


    public ReceivedSequenceMsg(List<SimonColorImpl> listToPlay, GameActivityPresenter presenter){
        this.sequence = listToPlay;
        this.presenter = presenter;
    }

    @Override
    public MessageType getType() {
        return MessageType.RECEIVED_SEQUENCE_MSG;
    }

    public List<SimonColorImpl> getSequence() {
        return sequence;
    }

    public GameActivityPresenter getPresenter() {
        return presenter;
    }
}
