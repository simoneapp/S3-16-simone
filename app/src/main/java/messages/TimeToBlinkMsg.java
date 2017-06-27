package messages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sapi9 on 21/06/2017.
 */

public class TimeToBlinkMsg implements IMessage {
    private List<Integer> sequence;

    public TimeToBlinkMsg(List<Integer> sequence){
        this.sequence = sequence;
    }

    @Override
    public MessageType getType() {
        return MessageType.TIME_TO_BLINK_MSG;
    }

    public List<Integer> getSequence(){
        return this.sequence;
    }
}
