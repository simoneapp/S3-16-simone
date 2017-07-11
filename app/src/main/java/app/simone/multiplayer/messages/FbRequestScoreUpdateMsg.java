package app.simone.multiplayer.messages;

import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.messages.MessageType;

/**
 * Created by nicola on 01/07/2017.
 */

public class FbRequestScoreUpdateMsg implements IMessage {

    private int score;

    public FbRequestScoreUpdateMsg(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_UPDATE_SCORE_MSG;
    }
}
