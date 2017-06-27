package messages;

import colors.Colors;

/**
 * Created by sapi9 on 23/06/2017.
 */

public class GuessColorMsg implements IMessage {
    private Colors guessColor;

    public GuessColorMsg(Colors color){
        this.guessColor = color;
    }

    @Override
    public MessageType getType() {
        return MessageType.GUESS_COLOR_MSG;
    }

    public Colors getGuessColor(){
        return this.guessColor;
    }
}
