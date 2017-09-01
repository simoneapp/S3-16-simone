package app.simone.multiplayer.messages;

import app.simone.singleplayer.messages.MessageType;

/**
 * Created by nicola on 01/07/2017.
 */

public class FbResponseGetUserScoreMsg extends FbOperationCompletedMsg<Integer> {

    public FbResponseGetUserScoreMsg(Integer data) {
        super(data);
    }

    public FbResponseGetUserScoreMsg(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_GET_SCORE_RESPONSE_MSG;
    }
}
