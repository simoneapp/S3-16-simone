package messages;

/**
 * Created by sapi9 on 23/06/2017.
 */

public class NextColorMsg implements IMessage {

    @Override
    public MessageType getType() {
        return MessageType.NEXT_COLOR_MSG;
    }

}
