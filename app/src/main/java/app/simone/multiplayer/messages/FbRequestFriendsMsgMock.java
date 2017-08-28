package app.simone.multiplayer.messages;

import android.os.Bundle;

import app.simone.multiplayer.model.GraphRequestWrapper;
import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.messages.MessageType;

/**
 * Created by nicola on 28/08/2017.
 */

public class FbRequestFriendsMsgMock implements IMessage {

    private Bundle bundle;

    private GraphRequestWrapper request;

    public FbRequestFriendsMsgMock(Bundle bundle, GraphRequestWrapper request) {
        this.bundle = bundle;
        this.request = request;
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_REQUEST_FRIENDS_MSG_MOCK;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public GraphRequestWrapper getRequest() {
        return request;
    }

}
