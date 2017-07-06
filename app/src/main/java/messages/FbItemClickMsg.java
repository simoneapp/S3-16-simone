package messages;

import app.simone.users.model.FacebookFriend;

/**
 * Created by nicola on 06/07/2017.
 */

public class FbItemClickMsg implements IMessage {

    private FacebookFriend friend;

    public FbItemClickMsg(FacebookFriend friend) {
        this.friend = friend;
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_ITEM_CLICK_MSG;
    }

    public FacebookFriend getFriend() {
        return friend;
    }

}
