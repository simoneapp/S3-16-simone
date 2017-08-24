package app.simone.singleplayer.messages;

import app.simone.singleplayer.view.IGameActivity;
import app.simone.shared.messages.IMessage;

/**
 * AttachViewMsg, used to send the current IGameActivity to the GameviewActor.
 * @author Michele Sapignoli
 */
public class AttachViewMsg implements IMessage {
    private IGameActivity activity;

    public AttachViewMsg(IGameActivity activity){
        this.activity = activity;
    }

    @Override
    public MessageType getType() {
        return MessageType.ATTACH_VIEW_MSG;
    }

    /**
     * Gets the current IGameActivity.
     * @return activity current IGameActivity
     */
    public IGameActivity getIActivity(){
        return this.activity;
    }
}
