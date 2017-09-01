package app.simone.singleplayer.messages;

import app.simone.shared.messages.IMessage;

/**
 * PlayerTurnMsg,
 * used to switch the turn from CPU to Player.
 * @author Michele Sapignoli
 */

public class PlayerTurnMsg implements IMessage {
    @Override
    public MessageType getType() {
        return MessageType.PLAYER_TURN_MSG;
    }
}
