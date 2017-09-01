package app.simone.multiplayer.messages;

import akka.actor.ActorRef;
import app.simone.multiplayer.view.pager.MultiplayerPagerActivity;
import app.simone.shared.application.App;
import app.simone.shared.messages.IMessage;
import app.simone.shared.utils.Constants;
import app.simone.shared.utils.Utilities;
import app.simone.singleplayer.messages.MessageType;

/**
 * Created by nicola on 06/07/2017.
 */

public class FbViewSetupMsg implements IMessage {

    private MultiplayerPagerActivity activity;
    private ActorRef actorReference;

    public FbViewSetupMsg(MultiplayerPagerActivity activity) {
        this(activity, Utilities.getActor(Constants.FACEBOOK_ACTOR_NAME, App.getInstance().getActorSystem()));
    }

    public FbViewSetupMsg(MultiplayerPagerActivity activity, ActorRef actor) {
        this.activity = activity;
        this.actorReference = actor;
    }

    @Override
    public MessageType getType() {
        return MessageType.FB_VIEW_SETUP_MSG;
    }

    public MultiplayerPagerActivity getActivity() {
        return activity;
    }

    public ActorRef getActorReference() {
        return actorReference;
    }
}
