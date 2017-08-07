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
	private boolean isSinglePlay;

	public StartGameVsCPUMsg(){
	}

	public StartGameVsCPUMsg(boolean isSinglePlay){
		this.isSinglePlay = isSinglePlay;
	}

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

	public boolean isSinglePlay(){
		return this.isSinglePlay;
	}

}

