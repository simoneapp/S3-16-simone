package app.simone.singleplayer.messages;

import app.simone.shared.messages.IMessage;

/**
 * NextColorMsg,
 * used to make the GameViewActor blink the next color.
 * @author Michele Sapignoli
 */
public class NextColorMsg implements IMessage {

    @Override
    public MessageType getType() {
        return MessageType.NEXT_COLOR_MSG;
    }

}
