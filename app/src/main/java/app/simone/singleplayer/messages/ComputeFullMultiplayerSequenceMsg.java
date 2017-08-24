package app.simone.singleplayer.messages;
import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.view.IGameActivity;

/**
 * ComputeFullMultiplayerSequenceMsg,
 * used to trigger the computing of the sequence played by the players in multiplayer mode.
 * @author Michele Sapignoli
 */
public class ComputeFullMultiplayerSequenceMsg implements IMessage {
    private IGameActivity activity;
    private String matchKey;
    private int nColors;
    private boolean isSecondPlayer;

    public ComputeFullMultiplayerSequenceMsg(IGameActivity activity,String key, int nColors,boolean isSecondPlayer){
        this.activity = activity;
        this.matchKey=key;
        this.nColors = nColors;
        this.isSecondPlayer=isSecondPlayer;
    }

    @Override
    public MessageType getType() {
        return MessageType.COMPUTE_FULL_MULTIPLAYER_SEQUENCE_MSG;
    }

    public IGameActivity getActivity(){
        return this.activity;
    }

    public String getMatchKey(){ return this.matchKey; }

    public int getNColors(){
        return this.nColors;
    }

    public boolean isSecondPlayer() { return this.isSecondPlayer;}


}
