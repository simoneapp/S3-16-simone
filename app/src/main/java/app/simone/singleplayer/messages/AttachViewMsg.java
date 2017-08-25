package app.simone.singleplayer.messages;

import app.simone.singleplayer.view.GameActivity;
import app.simone.shared.messages.IMessage;

/**
 * AttachViewMsg, used to send the current GameActivity to the GameviewActor.
 * @author Michele Sapignoli
 */
public class AttachViewMsg implements IMessage {
    private GameActivity activity;

    public AttachViewMsg(GameActivity activity){
        this.activity = activity;
    }

    @Override
    public MessageType getType() {
        return MessageType.ATTACH_VIEW_MSG;
    }

    /**
     * Gets the current GameActivity.
     * @return activity current GameActivity
     */
    public GameActivity getIActivity(){
        return this.activity;
    }
}
