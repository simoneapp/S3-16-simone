package messages;

/**
 * Created by sapi9 on 23/06/2017.
 */

public class GuessColorMsg implements IMessage {
    private int guessColor;

    public GuessColorMsg(int color){
        this.guessColor = color;
    }

    @Override
    public MessageType getType() {
        return MessageType.GUESS_COLOR_MSG;
    }

    public int getGuessColor(){
        return this.guessColor;
    }
}
