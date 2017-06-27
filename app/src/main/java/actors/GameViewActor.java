package actors;

import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.simone.IGameActivity;
import akka.actor.UntypedActor;
import application.mApplication;
import messages.AttachViewMsg;
import messages.BlinkMsg;
import messages.GuessColorMsg;
import messages.IMessage;
import messages.NextColorMsg;
import messages.PlayerTurnMsg;
import messages.StartGameVsCPUMsg;
import utils.Constants;
import utils.Utilities;

/**
 * @author Michele Sapignoli
 */

public class GameViewActor extends UntypedActor{
    private IGameActivity gameActivity;
    private List<Integer> cpuSequence;
    private List<Integer> playerSequence;
    private int cpuColorIndex;
    private int playerColorIndex;
    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.playerSequence = new ArrayList<>();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        switch (((IMessage) message).getType()) {
            case ATTACH_VIEW_MSG:
                this.gameActivity = ((AttachViewMsg)message).getIActivity();

                /*
                StartGameVsCPUMsg to CPU Actor
                 */
                Utilities.getActorByName(Constants.PATH_ACTOR + Constants.CPU_ACTOR_NAME, mApplication.getActorSystem())
                        .tell(new StartGameVsCPUMsg(((AttachViewMsg)message).getRadiobtnIndex()), getSelf());
                Log.d("##VIEW ACTOR", "Current GameActivity registered + StartGameVSCPUMsg sent to CPUActor ACTOR");
                break;
            case BLINK_MSG:
                this.cpuColorIndex = 0;
                List<Integer> sequence = ((BlinkMsg)message).getSequence();
                Log.d("##VIEW ACTOR", "CPU Turn, colors to blink:" + sequence.toString());
                cpuSequence = ((BlinkMsg)message).getSequence();
                getSelf().tell(new NextColorMsg(), getSelf());
                break;
            case NEXT_COLOR_MSG:
                if(this.isPlayerTurn()){
                    getSelf().tell(new PlayerTurnMsg(), getSelf());
                } else {
                    this.blink(cpuSequence.get(cpuColorIndex));
                    Log.d("##VIEW ACTOR", "Blinked:" + cpuSequence.get(cpuColorIndex));
                    this.cpuColorIndex++;
                }
                break;
            case PLAYER_TURN_MSG:
                Log.d("##VIEW ACTOR", "Player turn");
                this.gameActivity.setPlayerTurn(true);
                break;
            case GUESS_COLOR_MSG:
                int color = ((GuessColorMsg)message).getGuessColor();
                Log.d("##VIEW ACTOR", "Player inserted :" +color);
                playerSequence.add(color);
                //TODO correct check
                if(playerSequence.get(playerColorIndex) == cpuSequence.get(playerColorIndex)){
                    if(playerSequence.size() == cpuSequence.size()){
                        gameActivity.setPlayerTurn(false);
                        Utilities.getActorByName(Constants.PATH_ACTOR + Constants.CPU_ACTOR_NAME, mApplication.getActorSystem())
                                .tell(new NextColorMsg(), getSelf());
                        this.playerSequence.clear();
                    }

                } else {
                    Log.d("##VIEW ACTOR", "Hai perso coglione");
                }
                break;
        }
    }

    private void blink(int color){
        Message m = new Message();
        m.what = color;
        this.gameActivity.getActorHandler().sendMessageDelayed(m, 500);
    }

    private boolean isPlayerTurn(){
        return cpuColorIndex == cpuSequence.size();
    }

}
