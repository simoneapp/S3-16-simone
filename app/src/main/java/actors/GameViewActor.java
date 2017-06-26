package actors;

import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
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
import messages.YourTurnMsg;
import scala.collection.immutable.Stream;
import utils.Constants;
import utils.Utilities;

/**
 * @author Michele Sapignoli
 */

public class GameViewActor extends UntypedActor{
    private IGameActivity gameActivity;
    private List<Integer> cpuSequence;
    private List<Integer> playerSequence = new ArrayList<>();
    private int cpuColorIndex;

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
                List<Integer> sequence = ((BlinkMsg)message).getSequence();
                Log.d("##VIEW ACTOR", "CPU Turn, colors to blink:" + sequence.toString());
                cpuSequence = ((BlinkMsg)message).getSequence();
                getSelf().tell(new NextColorMsg(), ActorRef.noSender());
                break;
            case NEXT_COLOR_MSG:
                this.blink(cpuSequence.get(cpuColorIndex));
                Log.d("##VIEW ACTOR", "Blinked:" + cpuSequence.get(cpuColorIndex));
                this.cpuColorIndex++;
                this.isPlayerTurn();
                getSelf().tell(new NextColorMsg(), ActorRef.noSender());
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
                if(playerSequence.size() == cpuSequence.size()){
                    gameActivity.setPlayerTurn(false);
                }
                Utilities.getActorByName(Constants.PATH_ACTOR + Constants.CPU_ACTOR_NAME, mApplication.getActorSystem())
                        .tell(new NextColorMsg(), getSelf());
                break;
        }
    }

    private void blink(int color){
        Message m = new Message();
        m.what = color;
        this.gameActivity.getActorHandler().sendMessageDelayed(m, 500);
    }

    private void isPlayerTurn(){
        if (cpuColorIndex == cpuSequence.size()){
            getSelf().tell(new PlayerTurnMsg(), ActorRef.noSender());
        }
    }

}
