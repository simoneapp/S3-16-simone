package app.simone.singleplayer.messages;

import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.controller.GameActivityPresenter;

/**
 * AttachViewMsg, used to send the current GameActivity to the GameviewActor.
 * @author Michele Sapignoli
 */
public class AttachPresenterMsg implements IMessage {
    private GameActivityPresenter presenter;

    public AttachPresenterMsg(GameActivityPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public MessageType getType() {
        return MessageType.ATTACH_VIEW_MSG;
    }

    /**
     * Gets the current GameActivity.
     * @return activity current GameActivity
     */
    public GameActivityPresenter getPresenter(){
        return this.presenter;
    }
}
