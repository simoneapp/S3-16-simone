package messages;

/**
 * Created by sapi9 on 26/06/2017.
 */

public class PlayerTurnMsg implements IMessage {
    @Override
    public MessageType getType() {
        return MessageType.PLAYER_TURN_MSG;
    }
}
