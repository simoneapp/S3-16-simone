package messages;

import colors.Color;

/**
 * Created by sapi9 on 23/06/2017.
 */

public class GuessColorMsg implements IMessage {
    private Color guessColor;

    public GuessColorMsg(Color color){
        this.guessColor = color;
    }

    @Override
    public MessageType getType() {
        return MessageType.GUESS_COLOR_MSG;
    }

    public Color getGuessColor(){
        return this.guessColor;
    }
}
