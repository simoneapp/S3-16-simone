package messages;

import java.util.List;

import colors.Color;

/**
 * Created by sapi9 on 21/06/2017.
 */

public class TimeToBlinkMsg implements IMessage {
    private List<Color> sequence;

    public TimeToBlinkMsg(List<Color> sequence){
        this.sequence = sequence;
    }

    @Override
    public MessageType getType() {
        return MessageType.TIME_TO_BLINK_MSG;
    }

    public List<Color> getSequence(){
        return this.sequence;
    }
}
