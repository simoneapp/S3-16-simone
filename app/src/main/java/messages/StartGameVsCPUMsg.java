package messages;

/**
 * Start Game Message.
 */
public class StartGameVsCPUMsg implements IMessage {
	private int nColors = 4;

	public StartGameVsCPUMsg(int nColors){
		this.nColors = nColors;
	}

	@Override
	public MessageType getType() {
		return MessageType.STARTGAMEVSCPU;
	}

	public int getnColors(){
		return this.nColors;
	}

}

