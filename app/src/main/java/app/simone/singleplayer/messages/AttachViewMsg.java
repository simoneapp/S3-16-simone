package app.simone.singleplayer.messages;

import app.simone.singleplayer.view.IGameActivity;
import app.simone.shared.messages.IMessage;

/**
 * Created by sapi9 on 21/06/2017.
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

    public IGameActivity getIActivity(){
        return this.activity;
    }
}
