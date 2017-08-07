package app.simone.multiplayer.messages;

import app.simone.multiplayer.view.pager.MultiplayerPagerActivity;
import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.messages.MessageType;

/**
 * Created by nicola on 06/07/2017.
 */

public class FbViewSetupMsg implements IMessage {

    private MultiplayerPagerActivity activity;

    public FbViewSetupMsg(MultiplayerPagerActivity activity) {
        this.activity = activity;
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_VIEW_SETUP_MSG;
    }

    public MultiplayerPagerActivity getActivity() {
        return activity;
    }

}
