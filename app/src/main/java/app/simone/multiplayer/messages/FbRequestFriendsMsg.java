package app.simone.multiplayer.messages;

import app.simone.multiplayer.view.newmatch.FriendsListFragment;
import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.messages.MessageType;

/**
 * Created by nicola on 01/07/2017.
 */

public class FbRequestFriendsMsg implements IMessage {

    private FriendsListFragment activity;

    public FbRequestFriendsMsg(FriendsListFragment activity) {
        this.activity = activity;
    }

    public FriendsListFragment getActivity() {
        return activity;
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_REQUEST_FRIENDS_MSG;
    }
}