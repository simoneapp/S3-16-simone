package app.simone.singleplayer.controller;

import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.simone.singleplayer.model.SimonColor;
import app.simone.singleplayer.model.SimonColorImpl;
import app.simone.singleplayer.view.GameActivity;
import akka.actor.UntypedActor;
import app.simone.shared.application.App;
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
 * GameViewActor.
 * Handles the interaction between CPUActor and Player.
 *
 * @author Michele Sapignoli
 */
public class GameViewActor extends UntypedActor {
    private GameActivity gameActivity;
    private List<SimonColorImpl> cpuSequence;
    private List<SimonColorImpl> playerSequence;
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
                /*
                Received AttachViewMsg from GameActivity
                 */
                this.gameActivity = ((AttachViewMsg) message).getIActivity();
                Log.d("##VIEW ACTOR", "Current GameActivityImpl registered + StartGameVSCPUMsg sent to CPUActor ACTOR");
                break;
            case TIME_TO_BLINK_MSG:
                /*
                Time to blink the cpuSequence until it's over
                 */
                this.cpuColorIndex = 0;
                paused = false;
                playerTurn = false;
                cpuSequence = ((TimeToBlinkMsg) message).getSequence();
                Log.d("##VIEW ACTOR", "CPU Turn, colors to blink:" + cpuSequence.toString());
                getSelf().tell(new NextColorMsg(), getSelf());
                break;
            case NEXT_COLOR_MSG:
                /*
                Next color to blink, if the index is = size --> Player turn
                 */
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
                /*
                Player turn, msg to the public handler of GameActivity
                 */
                Log.d("##VIEW ACTOR", "Player turn");
                playerColorIndex = 0;
                playerSequence.clear();
                Message msg = new Message();
                msg.what = Constants.PLAYER_TURN;
                msg.arg2 = cpuSequence.size() - 1;
                gameActivity.getHandler().sendMessage(msg);
                break;
            case GUESS_COLOR_MSG:
                /*
                Check of the color guessed by the player
                 */
                if (playerTurn) {
                    Log.d("##VIEW ACTOR", "Player inserted :" + ((GuessColorMsg) message).getGuessColor());

                    if (cpuSequence.size() - playerSequence.size() > 0) { /*Avoid super fast tap*/
                        playerSequence.add(((GuessColorMsg) message).getGuessColor());

                        if (playerSequence.get(playerColorIndex).equals(cpuSequence.get(playerColorIndex))) { /*1by1 check*/
                        /*
                        Correct -- If sequence is completed --> CPUActor has to compute another color
                                    else --> player can continue tapping
                         */
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
                            /*
                            Incorrect -- Player loss
                             */
                            playerSequence.clear();
                            cpuColorIndex = 0;
                            Message m = new Message();
                            m.what = Constants.WHATTASHAMEYOULOST_MSG;
                            m.arg1 = cpuSequence.size() - 1;
                            playerColorIndex = 0;
                            cpuSequence.clear();
                            gameActivity.getHandler().sendMessage(m);
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

    private void blink(SimonColor color) {
        Message m = new Message();
        m.what = Constants.CPU_TURN;
        m.arg1 = color.getButtonId();
        this.gameActivity.getHandler().sendMessageDelayed(m, Constants.STD_DELAY_BTN_TIME);
    }


}