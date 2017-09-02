package app.simone.singleplayer.messages;

import java.util.List;

import app.simone.singleplayer.model.SimonColor;
import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.model.SimonColorImpl;

/**
 * TimeToBlinkMsg,
 * used to trigger the blinking time of the CPUActor.
 * @author Michele Sapignoli
 */

public class TimeToBlinkMsg implements IMessage {
    private List<SimonColorImpl> sequence;

    public TimeToBlinkMsg(List<SimonColorImpl> sequence){
        this.sequence = sequence;
    }

    @Override
    public MessageType getType() {
        return MessageType.TIME_TO_BLINK_MSG;
    }

    public List<SimonColorImpl> getSequence(){
        return this.sequence;
    }
}
