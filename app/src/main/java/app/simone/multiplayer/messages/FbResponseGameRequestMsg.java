package app.simone.multiplayer.messages;

import app.simone.singleplayer.messages.MessageType;

/**
 * Created by nicola on 05/07/2017.
 */

public class FbResponseGameRequestMsg extends FbOperationCompletedMsg {

    public FbResponseGameRequestMsg(Boolean success) {
        super(true);
    }

    public FbResponseGameRequestMsg(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_OPEN_REQUESTS_DIALOG;
    }
}
