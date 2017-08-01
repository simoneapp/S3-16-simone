package app.simone.singleplayer.messages;

import java.util.ArrayList;
import java.util.List;

import app.simone.shared.messages.IMessage;
import app.simone.shared.utils.Constants;
import app.simone.singleplayer.model.SColor;

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

