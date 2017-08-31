package app.simone.singleplayer.messages;

import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.controller.GameActivityPresenter;

/**
 * ComputeFullMultiplayerSequenceMsg,
 * used to trigger the computing of the sequence played by the players in multiplayer mode.
 * @author Michele Sapignoli
 */
public class ComputeFullMultiplayerSequenceMsg implements IMessage {
    private GameActivityPresenter presenter;
    private String matchKey;
    private int nColors;
    private boolean isSecondPlayer;

    public ComputeFullMultiplayerSequenceMsg(GameActivityPresenter presenter, String key, int nColors, boolean isSecondPlayer){
        this.presenter = presenter;
        this.matchKey=key;
        this.nColors = nColors;
        this.isSecondPlayer=isSecondPlayer;
    }

    @Override
    public MessageType getType() {
        return MessageType.COMPUTE_FULL_MULTIPLAYER_SEQUENCE_MSG;
    }

    public GameActivityPresenter getPresenter(){
        return this.presenter;
    }

    public String getMatchKey(){ return this.matchKey; }

    public int getNColors(){
        return this.nColors;
    }

    public boolean isSecondPlayer() { return this.isSecondPlayer;}


}
