package messages;

import app.simone.users.model.FacebookUser;

/**
 * Created by nicola on 06/07/2017.
 */

public class FbItemClickMsg implements IMessage {

    private FacebookUser friend;

    public FbItemClickMsg(FacebookUser friend) {
        this.friend = friend;
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_ITEM_CLICK_MSG;
    }

    public FacebookUser getFriend() {
        return friend;
    }

}
