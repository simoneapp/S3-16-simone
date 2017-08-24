package app.simone.singleplayer.messages;

import java.util.List;

import app.simone.singleplayer.model.SColor;
import app.simone.shared.messages.IMessage;

/**
 * TimeToBlinkMsg,
 * used to trigger the blinking time of the CPUActor.
 * @author Michele Sapignoli
 */

public class TimeToBlinkMsg implements IMessage {
    private List<SColor> sequence;

    public TimeToBlinkMsg(List<SColor> sequence){
        this.sequence = sequence;
    }

    @Override
    public MessageType getType() {
        return MessageType.TIME_TO_BLINK_MSG;
    }

    public List<SColor> getSequence(){
        return this.sequence;
    }
}
