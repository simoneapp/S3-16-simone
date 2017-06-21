package messages;

import java.util.List;

/**
 * Created by sapi9 on 21/06/2017.
 */

public class BlinkMsg implements IMessage {
    private List<Integer> sequence;

    public BlinkMsg(List<Integer> sequence){
        this.sequence = sequence;
    }

    @Override
    public MessageType getType() {
        return MessageType.BLINK_MSG;
    }

    public List getSequence(){
        return this.sequence;
    }
}
