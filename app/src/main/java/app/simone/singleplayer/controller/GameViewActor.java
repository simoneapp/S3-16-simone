package app.simone.singleplayer.controller;

import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.simone.singleplayer.view.IGameActivity;
import akka.actor.UntypedActor;
import app.simone.shared.application.App;
import app.simone.singleplayer.model.SColor;
import app.simone.singleplayer.messages.AttachViewMsg;
import app.simone.singleplayer.messages.PauseMsg;
import app.simone.singleplayer.messages.TimeToBlinkMsg;
import app.simone.singleplayer.messages.GimmeNewColorMsg;
import app.simone.singleplayer.messages.GuessColorMsg;
import app.simone.shared.messages.IMessage;
import app.simone.singleplayer.messages.NextColorMsg;
import app.simone.singleplayer.messages.PlayerTurnMsg;
import app.simone.shared.utils.Constants;
import app.simone.shared.utils.Utilities;

/**
 * @author Michele Sapignoli
 */

public class GameViewActor extends UntypedActor {
    private IGameActivity gameActivity;
    private List<SColor> cpuSequence;
    private List<SColor> playerSequence;
    private int cpuColorIndex;
    private int playerColorIndex;
    private boolean playerTurn;

    private boolean paused = false;

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
                paused = false;
                playerTurn = false;
                cpuSequence = ((TimeToBlinkMsg) message).getSequence();
                Log.d("##VIEW ACTOR", "CPU Turn, colors to blink:" + cpuSequence.toString());
                getSelf().tell(new NextColorMsg(), getSelf());
                break;
            case NEXT_COLOR_MSG:
                if (!paused) {
                    if (cpuColorIndex >= cpuSequence.size() /*Player turn if true*/) {
                        playerTurn = true;
                        getSelf().tell(new PlayerTurnMsg(), getSelf());
                    } else {
                        this.blink(cpuSequence.get(cpuColorIndex));
                        Log.d("##VIEW ACTOR", "Blinked:" + cpuSequence.get(cpuColorIndex));
                        this.cpuColorIndex++;
                    }
                }
                break;
            case PLAYER_TURN_MSG:
                Log.d("##VIEW ACTOR", "Player turn");
                playerColorIndex = 0;
                playerSequence.clear();
                gameActivity.getHandler().sendEmptyMessage(Constants.PLAYER_TURN);
                break;
            case GUESS_COLOR_MSG:
                if (playerTurn) {
                    Log.d("##VIEW ACTOR", "Player inserted :" + ((GuessColorMsg) message).getGuessColor());

                    if (cpuSequence.size() - playerSequence.size() > 0) { /*Avoid super fast tap*/
                        playerSequence.add(((GuessColorMsg) message).getGuessColor());

                        if (playerSequence.get(playerColorIndex).equals(cpuSequence.get(playerColorIndex))) { /*1by1 check*/
                            if (cpuSequence.size() - playerSequence.size() == 0) {
                                Message m = new Message();
                                m.what = Constants.CPU_TURN;
                                m.arg2 = playerColorIndex;
                                gameActivity.getHandler().sendMessage(m);
                                Utilities.getActorByName(Constants.PATH_ACTOR + Constants.CPU_ACTOR_NAME, App.getInstance().getActorSystem())
                                        .tell(new GimmeNewColorMsg(), getSelf());
                                this.playerSequence.clear();
                            }
                            this.playerColorIndex++;
                        } else {
                            //Player Loss

                            cpuColorIndex = 0;
                            cpuSequence.clear();
                            Message msg = new Message();
                            msg.what = Constants.WHATTASHAMEYOULOST_MSG;
                            msg.arg1 = cpuSequence.size();
                            playerColorIndex = 0;
                            playerSequence.clear();
                            gameActivity.getHandler().sendMessage(msg);
                        }
                    }
                }
                break;
            case PAUSE_MSG:
                Log.d("##VIEW ACTOR", "PAUSE");
                this.paused = ((PauseMsg) message).isPausing();
                if (!this.paused) {
                    getSelf().tell(new NextColorMsg(), getSelf());
                }
                break;
        }
    }

    private void blink(SColor color) {
        Message m = new Message();
        m.what = Constants.CPU_TURN;
        m.arg1 = color.getButtonId();
        this.gameActivity.getHandler().sendMessageDelayed(m, Constants.STD_DELAY_BTN_TIME);
    }


    public int getPlayerColorIndex(){ return playerColorIndex; }

}