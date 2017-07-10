package app.simone.multiplayer.messages;

import java.util.List;

import app.simone.multiplayer.model.FacebookUser;
import app.simone.singleplayer.messages.MessageType;

/**
 * Created by nicola on 01/07/2017.
 */

public class FbResponseFriendsMsg extends FbOperationCompletedMsg<List<FacebookUser>> {

    public FbResponseFriendsMsg(List<FacebookUser> data) {
        super(data);
    }

    public FbResponseFriendsMsg(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_GET_FRIENDS_RESPONSE_MSG;
    }
}
