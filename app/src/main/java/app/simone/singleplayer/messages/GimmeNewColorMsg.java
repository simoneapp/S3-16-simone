package app.simone.singleplayer.messages;

import app.simone.shared.messages.IMessage;

/**
 * Created by sapi9 on 27/06/2017.
 */

public class GimmeNewColorMsg implements IMessage {
    @Override
    public MessageType getType() {
        return MessageType.GIMME_NEW_COLOR_MSG;
    }
}
