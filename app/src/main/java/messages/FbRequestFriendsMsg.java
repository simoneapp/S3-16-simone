package messages;

/**
 * Created by nicola on 01/07/2017.
 */

public class FbRequestFriendsMsg implements IMessage {
    @Override
    public MessageType getType() {
        return MessageType.FB_REQUEST_FRIENDS_MSG;
    }
}