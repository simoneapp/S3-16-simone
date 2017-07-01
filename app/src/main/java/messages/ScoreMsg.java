package messages;

/**
 * Created by gzano on 01/07/2017.
 */

public class ScoreMsg implements IMessage {

    private int score;

    public ScoreMsg(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }


    @Override
    public MessageType getType() {
        return MessageType.SCORE_MSG;
    }
}
