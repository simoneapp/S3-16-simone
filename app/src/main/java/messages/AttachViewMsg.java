package messages;

import app.simone.IGameActivity;

/**
 * Created by sapi9 on 21/06/2017.
 */

public class AttachViewMsg implements IMessage {
    private IGameActivity activity;
    private int radiobtnIndex = 0;

    public AttachViewMsg(IGameActivity activity){
        this.activity = activity;
    }
    public AttachViewMsg(IGameActivity activity, int radiobtnIndex){
        this.activity = activity;
        this.radiobtnIndex = radiobtnIndex;
    }

    @Override
    public MessageType getType() {
        return MessageType.ATTACH_VIEW_MSG;
    }

    public IGameActivity getIActivity(){
        return this.activity;
    }

    public int getRadiobtnIndex(){
        return this.radiobtnIndex;
    }
}
