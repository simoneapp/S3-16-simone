package messages;

import Model.interfaces.IActivity;

/**
 * Created by sapi9 on 21/06/2017.
 */

public class AttachViewMsg implements IMessage {
    private IActivity activity;

    public AttachViewMsg(IActivity activity){
        this.activity = activity;
    }

    @Override
    public MessageType getType() {
        return MessageType.ATTACH_VIEW_MSG;
    }

    public IActivity getIActivity(){
        return this.activity;

    }
}
