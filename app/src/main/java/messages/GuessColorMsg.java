package messages;

import colors.SColor;

/**
 * Created by sapi9 on 23/06/2017.
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
