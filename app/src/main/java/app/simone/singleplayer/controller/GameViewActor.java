package app.simone.singleplayer.controller;

import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import app.simone.shared.application.App;
import app.simone.shared.messages.IMessage;
import app.simone.shared.utils.Constants;
import app.simone.shared.utils.Utilities;
import app.simone.singleplayer.messages.AttachPresenterMsg;
import app.simone.singleplayer.messages.GimmeNewColorMsg;
import app.simone.singleplayer.messages.GuessColorMsg;
import app.simone.singleplayer.messages.NextColorMsg;
import app.simone.singleplayer.messages.PauseMsg;
import app.simone.singleplayer.messages.PlayerTurnMsg;
import app.simone.singleplayer.messages.TimeToBlinkMsg;
import app.simone.singleplayer.model.MessageBuilder;
import app.simone.singleplayer.model.SimonColor;
import app.simone.singleplayer.model.SimonColorImpl;

/**
 * GameViewActor.
 * Handles the interaction between CPUActor and Player.
 *
 * @author Michele Sapignoli
 */
public class GameViewActor extends UntypedActor {
    private GameActivityPresenter presenter;
    private List<SimonColorImpl> cpuSequence;
    private List<SimonColorImpl> playerSequence;
    private int cpuColorIndex;
    private int playerColorIndex;
    private boolean playerTurn;
    private boolean paused = false;
    private ActorRef currentSender;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.playerSequence = new ArrayList<>();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        currentSender = sender();

        switch (((IMessage) message).getType()) {
            case ATTACH_VIEW_MSG:
                attachPresenter((AttachPresenterMsg) message);
                break;
            case TIME_TO_BLINK_MSG:
                timeToBlink((TimeToBlinkMsg)message);
                break;
            case NEXT_COLOR_MSG:
                nextColor();
                break;
            case PLAYER_TURN_MSG:
                handlePlayerTurn();
                break;
            case GUESS_COLOR_MSG:
                guessColor((GuessColorMsg)message);
                break;
            case PAUSE_MSG:
                pause((PauseMsg)message);
                break;
        }
    }

    private void blink(SimonColor color) {
        Message m = MessageBuilder.withArg1(Constants.CPU_TURN, color.getButtonId());
        presenter.blinkDelayed(m, Constants.STD_DELAY_BTN_TIME);
    }

    /**
     * Received AttachPresenterMsg from GameActivityPresenter
     * @param message
     */
    private void attachPresenter(AttachPresenterMsg message) {
        this.presenter = message.getPresenter();
    }

    /**
     * Time to blink the cpuSequence until it's over
     * @param message
     */
    private void timeToBlink(TimeToBlinkMsg message) {
        this.cpuColorIndex = 0;
        paused = false;
        playerTurn = false;
        cpuSequence = message.getSequence();

        IMessage msg = new NextColorMsg();
        getSelf().tell(msg, currentSender);
    }

    /**
     * Next color to blink, if the index is = size --> Player turn
     */
    private void nextColor(){
        if (!paused) {
            if (cpuColorIndex >= cpuSequence.size() /*Player turn if true*/) {
                playerTurn = true;
                IMessage playerTurnMsg = new PlayerTurnMsg();
                getSelf().tell(playerTurnMsg, getSelf());

                if(currentSender != null && currentSender != ActorRef.noSender()) {
                    currentSender.tell(playerTurnMsg, getSelf());
                }
            } else {
                this.blink(cpuSequence.get(cpuColorIndex));
                this.cpuColorIndex++;
            }
        }
    }

    /**
     * Player turn, msg to the public handler of GameActivity
     */
    private void handlePlayerTurn() {
        playerColorIndex = 0;
        playerSequence.clear();
        Message msg = MessageBuilder.withArg2(Constants.PLAYER_TURN, cpuSequence.size() - 1);
        presenter.getHandler().sendMessage(msg);
    }

    /**
     * Check of the color guessed by the player
     * @param message
     */
    private void guessColor(GuessColorMsg message) {
        if (playerTurn) {
            if (cpuSequence.size() - playerSequence.size() > 0) { /*Avoid super fast tap*/
                playerSequence.add(message.getGuessColor());

                SimonColor playerColor = playerSequence.get(playerColorIndex);
                SimonColor cpuColor = cpuSequence.get(playerColorIndex);

                if (playerColor.equals(cpuColor)) { /*1by1 check*/
                        /*
                        Correct -- If sequence is completed --> CPUActor has to compute another color
                                    else --> player can continue tapping
                         */
                    if (cpuSequence.size() - playerSequence.size() == 0) {

                        Message m = MessageBuilder.withArg2(Constants.CPU_TURN, playerColorIndex);
                        presenter.getHandler().sendMessage(m);

                        Utilities.getActor(Constants.CPU_ACTOR_NAME, App.getInstance().getActorSystem())
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

                    Message m = MessageBuilder.withArg1(Constants.WHATTASHAMEYOULOST_MSG, cpuSequence.size() - 1);
                    playerColorIndex = 0;
                    cpuSequence.clear();
                    presenter.getHandler().sendMessage(m);
                }
            }
        }
    }

    private void pause(PauseMsg message) {
        this.paused = message.isPausing();
        if (!this.paused) {
            getSelf().tell(new NextColorMsg(), getSelf());
        }
    }

}