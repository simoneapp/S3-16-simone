package app.simone.singleplayer.messages;

import akka.actor.ActorSystem;
import app.simone.shared.application.App;
import app.simone.shared.messages.IMessage;
import app.simone.shared.utils.Constants;

/**
 * Start Game Message.
 * @author Michele Sapignoli
 */
public class StartGameVsCPUMsg implements IMessage {
	private int radiobtnIndex = 0;
	private boolean isSinglePlay;
	private ActorSystem system;

	public StartGameVsCPUMsg(){
	}

	public StartGameVsCPUMsg(boolean isSinglePlay){
		this.isSinglePlay = isSinglePlay;
		this.system = App.getInstance().getActorSystem();
	}

	public StartGameVsCPUMsg(int radiobtnIndex){
		this.radiobtnIndex = radiobtnIndex;
		this.system = App.getInstance().getActorSystem();
	}

	public StartGameVsCPUMsg(boolean isSinglePlay, ActorSystem system){
		this.isSinglePlay = isSinglePlay;
		this.system = system;
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

	public ActorSystem getSystem() {
		return system;
	}
}

