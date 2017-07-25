package app.simone.singleplayer.messages;

import app.simone.shared.messages.IMessage;
import app.simone.shared.utils.Constants;

/**
 * Start Game Message.
 */
public class StartGameVsCPUMsg implements IMessage {
	private int radiobtnIndex = 0;

	public StartGameVsCPUMsg(){}

	public StartGameVsCPUMsg(int radiobtnIndex){
		this.radiobtnIndex = radiobtnIndex;
	}

	@Override
	public MessageType getType() {
		return MessageType.START_GAME_VS_CPU;
	}


	public int getnColors(){
		return radiobtnIndex == 0? Constants.CLASSIC_MODE : Constants.HARD_MODE;
	}

}

