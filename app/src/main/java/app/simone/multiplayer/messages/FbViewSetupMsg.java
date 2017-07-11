package app.simone.multiplayer.messages;

import app.simone.multiplayer.view.FacebookLoginActivity;
import app.simone.singleplayer.messages.MessageType;
import app.simone.shared.messages.IMessage;

/**
 * Created by nicola on 06/07/2017.
 */

public class FbViewSetupMsg implements IMessage {

    private FacebookLoginActivity activity;

    public FbViewSetupMsg(FacebookLoginActivity activity) {
        this.activity = activity;
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_VIEW_SETUP_MSG;
    }

    public FacebookLoginActivity getActivity() {
        return activity;
    }

}
