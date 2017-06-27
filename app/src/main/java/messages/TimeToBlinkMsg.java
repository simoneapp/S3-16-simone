package messages;

import java.util.ArrayList;
import java.util.List;

import colors.Colors;

/**
 * Created by sapi9 on 21/06/2017.
 */

public class TimeToBlinkMsg implements IMessage {
    private List<Colors> sequence;

    public TimeToBlinkMsg(List<Colors> sequence){
        this.sequence = sequence;
    }

    @Override
    public MessageType getType() {
        return MessageType.TIME_TO_BLINK_MSG;
    }

    public List<Colors> getSequence(){
        return this.sequence;
    }
}
