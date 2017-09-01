package app.simone.singleplayer.messages;

import app.simone.shared.messages.IMessage;

/**
 * GimmeNewColorMsg,
 * used to ask a new SColor of the sequence to the CPUActor.
 * @author Michele Sapignoli
 */
public class GimmeNewColorMsg implements IMessage {
    @Override
    public MessageType getType() {
        return MessageType.GIMME_NEW_COLOR_MSG;
    }
}
