package messages;

/**
 * Start Game Message.
 */
public class StartGameVsCPUMsg implements IMessage {
	private int mode = 0;

	public StartGameVsCPUMsg(int mode){
		this.mode = mode;
	}

	@Override
	public MessageType getType() {
		return MessageType.START_GAME_VS_CPU;
	}


	public int getnColors(){
		return mode == 0? 4 : 6;
	}

}

