package app.simone.multiplayer.messages;

import java.util.List;

import app.simone.multiplayer.model.FacebookUser;
import app.simone.multiplayer.view.FacebookLoginActivity;
import app.simone.singleplayer.messages.MessageType;

/**
 * Created by nicola on 01/07/2017.
 */

public class FbResponseFriendsMsg extends FbOperationCompletedMsg<List<FacebookUser>> {

    private FacebookLoginActivity activity;

    public FbResponseFriendsMsg(List<FacebookUser> data, FacebookLoginActivity activity) {
        super(data);
        this.activity = activity;
    }

    public FacebookLoginActivity getActivity() {
        return activity;
    }

    public FbResponseFriendsMsg(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_GET_FRIENDS_RESPONSE_MSG;
    }
}
