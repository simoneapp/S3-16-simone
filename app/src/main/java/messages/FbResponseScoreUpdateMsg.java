package messages;

/**
 * Created by nicola on 01/07/2017.
 */

public class FbResponseScoreUpdateMsg extends FbOperationCompletedMsg<Object> {

    public FbResponseScoreUpdateMsg(Object data) {
        super(data);
    }

    public FbResponseScoreUpdateMsg(String errorMessage) {
        super(errorMessage);
    }
    
    @Override
    public MessageType getType() {
        return MessageType.FB_SCORE_UPDATE_RESPONSE_MSG;
    }
}
