package messages;

import app.simone.users.FacebookLoginActivity;

/**
 * Created by nicola on 06/07/2017.
 */

public class FbViewSetupMsg implements IMessage{

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
