package messages;

import java.util.List;

import app.simone.users.model.FacebookFriend;

/**
 * Created by nicola on 01/07/2017.
 */

public class FbResponseFriendsMsg extends FbOperationCompletedMsg<List<FacebookFriend>> {

    public FbResponseFriendsMsg(List<FacebookFriend> data) {
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
