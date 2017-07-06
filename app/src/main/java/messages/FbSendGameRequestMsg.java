package messages;

/**
 * Created by nicola on 06/07/2017.
 */

public class FbSendGameRequestMsg implements IMessage {
    @Override
    public MessageType getType() {
        return MessageType.FB_GAME_REQUEST_MSG;
    }
}
