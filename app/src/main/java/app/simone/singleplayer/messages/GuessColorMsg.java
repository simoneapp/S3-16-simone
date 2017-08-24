package app.simone.singleplayer.messages;

import app.simone.singleplayer.model.SColor;
import app.simone.shared.messages.IMessage;

/**
 * GuessColorMsg,
 * containing the color guessed by the player.
 * @author Michele Sapignoli
 */
public class GuessColorMsg implements IMessage {
    private SColor guessColor;

    public GuessColorMsg(SColor color){
        this.guessColor = color;
    }

    @Override
    public MessageType getType() {
        return MessageType.GUESS_COLOR_MSG;
    }

    public SColor getGuessColor(){
        return this.guessColor;
    }
}
