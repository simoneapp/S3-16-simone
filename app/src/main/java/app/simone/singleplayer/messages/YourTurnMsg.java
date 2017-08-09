package app.simone.singleplayer.messages;

import java.util.List;

import app.simone.shared.messages.IMessage;

/**
 * Created by sapi9 on 22/06/2017.
 */

public class YourTurnMsg implements IMessage {
    private List<Integer> sequence;

    public YourTurnMsg(List<Integer> sequence){
        this.sequence = sequence;
    }
    @Override
    public MessageType getType() {
        return MessageType.YOUR_TURN_MSG;
    }
}
