package app.simone.multiplayer.messages;

import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.messages.MessageType;

/**
 * Created by nicola on 06/07/2017.
 */

public class FbSendGameRequestMsg implements IMessage {
    @Override
    public MessageType getType() {
        return MessageType.FB_GAME_REQUEST_MSG;
    }
}
