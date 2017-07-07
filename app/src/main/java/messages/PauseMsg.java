package messages;

/**
 * Created by sapi9 on 02/07/2017.
 */

public class PauseMsg implements IMessage {
    private boolean pausing;

    public PauseMsg(boolean isPausing){
        this.pausing = isPausing;
    }

    @Override
    public MessageType getType() {
        return MessageType.PAUSE_MSG;
    }

    public boolean isPausing(){
        return this.pausing;
    }
}
