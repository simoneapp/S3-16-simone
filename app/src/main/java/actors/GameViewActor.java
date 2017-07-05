package actors;

import android.os.Message;
import android.util.Log;

import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.List;

import app.simone.IGameActivity;
import akka.actor.UntypedActor;
import application.mApplication;
import io.realm.Realm;
import colors.SColor;
import messages.AttachViewMsg;
import messages.TimeToBlinkMsg;
import messages.GimmeNewColorMsg;
import messages.GuessColorMsg;
import messages.IMessage;
import messages.NextColorMsg;
import messages.PlayerTurnMsg;
import utils.Constants;
import utils.Utilities;

/**
 * @author Michele Sapignoli
 */

public class GameViewActor extends UntypedActor {
    private IGameActivity gameActivity;
    private List<SColor> cpuSequence;
    private List<SColor> playerSequence;
    private int cpuColorIndex;
    private int playerColorIndex;
    private int currentScore = 0;
    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.playerSequence = new ArrayList<>();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        switch (((IMessage) message).getType()) {
            case ATTACH_VIEW_MSG:
                this.gameActivity = ((AttachViewMsg) message).getIActivity();
                Log.d("##VIEW ACTOR", "Current GameActivity registered + StartGameVSCPUMsg sent to CPUActor ACTOR");
                break;
            case TIME_TO_BLINK_MSG:
                this.cpuColorIndex = 0;
                cpuSequence = ((TimeToBlinkMsg) message).getSequence();
                Log.d("##VIEW ACTOR", "CPU Turn, colors to blink:" + cpuSequence.toString());
                getSelf().tell(new NextColorMsg(), getSelf());
                break;
            case NEXT_COLOR_MSG:
                if (this.isPlayerTurn()) {
                    getSelf().tell(new PlayerTurnMsg(), getSelf());
                } else {
                    this.blink(cpuSequence.get(cpuColorIndex));
                    Log.d("##VIEW ACTOR", "Blinked:" + cpuSequence.get(cpuColorIndex));
                    this.cpuColorIndex++;
                }
                break;
            case PLAYER_TURN_MSG:
                Log.d("##VIEW ACTOR", "Player turn");
                playerColorIndex = 0;
                playerSequence.clear();
                gameActivity.getHandler().sendEmptyMessage(Constants.PLAYER_TURN);
                break;
            case GUESS_COLOR_MSG:
                SColor color = ((GuessColorMsg) message).getGuessColor();
                Log.d("##VIEW ACTOR", "Player inserted :" + color);
                playerSequence.add(color);

                if (playerSequence.get(playerColorIndex).equals(cpuSequence.get(playerColorIndex))) {


                    if (playerSequence.size() == cpuSequence.size()) {
                        currentScore++;
                        Log.d("CURRENT SCORE", Integer.toString(currentScore));
                        gameActivity.getHandler().sendEmptyMessage(Constants.CPU_TURN);
                        Utilities.getActorByName(Constants.PATH_ACTOR + Constants.CPU_ACTOR_NAME, mApplication.getActorSystem())
                                .tell(new GimmeNewColorMsg(), getSelf());
                        this.playerSequence.clear();

                    }
                    this.playerColorIndex++;

                } else {
                    playerColorIndex = 0;
                    playerSequence.clear();
                    cpuColorIndex = 0;
                    cpuSequence.clear();
                    gameOver(currentScore);
                }
                break;
        }
    }

    private void blink(SColor color) {
        Message m = new Message();
        m.what = Constants.CPU_TURN;
        m.arg1 = color.getValue();

        this.gameActivity.getHandler().sendMessageDelayed(m, 500);
    }

    private boolean isPlayerTurn() {
        return cpuColorIndex == cpuSequence.size();
    }

    private void gameOver(int currentScore) {
        Message m = new Message();
        m.what = Constants.WHATTASHAMEYOULOST_MSG;
        m.arg2 = currentScore;
        this.gameActivity.getHandler().sendMessage(m);
        this.currentScore=0;

    }

}