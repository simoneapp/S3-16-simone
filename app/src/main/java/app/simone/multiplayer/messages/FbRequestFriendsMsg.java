package app.simone.multiplayer.messages;

import app.simone.multiplayer.view.FacebookLoginActivity;
import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.messages.MessageType;

/**
 * Created by nicola on 01/07/2017.
 */

public class FbRequestFriendsMsg implements IMessage {

    private FacebookLoginActivity activity;

    public FbRequestFriendsMsg(FacebookLoginActivity activity) {
        this.activity = activity;
    }

    public FacebookLoginActivity getActivity() {
        return activity;
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_REQUEST_FRIENDS_MSG;
    }
}