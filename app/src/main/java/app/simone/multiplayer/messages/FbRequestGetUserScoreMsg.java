package app.simone.multiplayer.messages;

import app.simone.multiplayer.model.FacebookUser;
import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.messages.MessageType;

/**
 * Created by nicola on 01/07/2017.
 */

public class FbRequestGetUserScoreMsg implements IMessage {

    private FacebookUser friend;

    public FbRequestGetUserScoreMsg(FacebookUser friend) {
        this.friend = friend;
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_GET_FRIEND_SCORE_MSG;
    }

    public FacebookUser getFriend() {
        return friend;
    }
}
