package app.simone.singleplayer.messages;

import app.simone.shared.messages.IMessage;

/**
 * Created by nicola on 30/08/2017.
 */

public class TestMessage implements IMessage{

    @Override
    public MessageType getType() {
        return MessageType.TEST;
    }
}
