package app.simone.multiplayer.messages;

import app.simone.multiplayer.model.FacebookUser;
import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.messages.MessageType;

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
