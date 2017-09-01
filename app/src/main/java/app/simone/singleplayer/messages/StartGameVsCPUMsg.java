package app.simone.singleplayer.messages;

import akka.actor.ActorRef;
import app.simone.shared.application.App;
import app.simone.shared.messages.IMessage;
import app.simone.shared.utils.Constants;
import app.simone.shared.utils.Utilities;

/**
 * Start Game Message.
 * @author Michele Sapignoli
 */
public class StartGameVsCPUMsg implements IMessage {
	private int radiobtnIndex = 0;
	private boolean isSinglePlay;
	private ActorRef replyingActor;

	public StartGameVsCPUMsg(){
	}

	public StartGameVsCPUMsg(boolean isSinglePlay){
		this.isSinglePlay = isSinglePlay;
		this.replyingActor = Utilities.getActor(
				Constants.GAMEVIEW_ACTOR_NAME,
				App.getInstance().getActorSystem());
	}

	public StartGameVsCPUMsg(int radiobtnIndex){
		this.radiobtnIndex = radiobtnIndex;
		this.replyingActor = Utilities.getActor(
				Constants.GAMEVIEW_ACTOR_NAME,
				App.getInstance().getActorSystem());
	}

	public StartGameVsCPUMsg(boolean isSinglePlay, ActorRef replyingActor){
		this.isSinglePlay = isSinglePlay;
		this.replyingActor = replyingActor;
	}


	@Override
	public MessageType getType() {
		return MessageType.START_GAME_VS_CPU;
	}


	public int getnColors(){
		return radiobtnIndex == 0? Constants.CLASSIC_MODE : Constants.HARD_MODE;
	}

	public boolean isSinglePlay(){
		return this.isSinglePlay;
	}

	public ActorRef getReplyingActor() {
		return replyingActor;
	}
}

