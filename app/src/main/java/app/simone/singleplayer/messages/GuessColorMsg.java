package app.simone.singleplayer.messages;

import app.simone.singleplayer.model.SimonColorImpl;
import app.simone.shared.messages.IMessage;

/**
 * GuessColorMsg,
 * containing the color guessed by the player.
 * @author Michele Sapignoli
 */
public class GuessColorMsg implements IMessage {
    private SimonColorImpl guessColor;

    public GuessColorMsg(SimonColorImpl color){
        this.guessColor = color;
    }

    @Override
    public MessageType getType() {
        return MessageType.GUESS_COLOR_MSG;
    }

    public SimonColorImpl getGuessColor(){
        return this.guessColor;
    }
}
