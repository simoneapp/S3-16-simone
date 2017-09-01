package app.simone.singleplayer.messages;

import app.simone.shared.messages.IMessage;

/**
 * PauseMsg,
 * used to pause the behaviour of the actors.
 * @author Michele Sapignoli
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
