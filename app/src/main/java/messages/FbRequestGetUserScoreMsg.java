package messages;

import app.simone.users.model.FacebookFriend;

/**
 * Created by nicola on 01/07/2017.
 */

public class FbRequestGetUserScoreMsg implements IMessage {

    private FacebookFriend friend;

    public FbRequestGetUserScoreMsg(FacebookFriend friend) {
        this.friend = friend;
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_GET_FRIEND_SCORE_MSG;
    }

    public FacebookFriend getFriend() {
        return friend;
    }
}
