package app.simone.multiplayer.messages;

import android.os.Bundle;

import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.messages.MessageType;

/**
 * Created by nicola on 28/08/2017.
 */

public class FbRequestFriendsMsgMock implements IMessage {

    private Bundle bundle;

    public FbRequestFriendsMsgMock(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_REQUEST_FRIENDS_MSG_MOCK;
    }

    public Bundle getBundle() {
        return bundle;
    }

}
