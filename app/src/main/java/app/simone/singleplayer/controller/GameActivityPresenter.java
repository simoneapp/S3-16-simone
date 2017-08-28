package app.simone.singleplayer.controller;

import android.os.Handler;
import android.os.Message;

import akka.actor.ActorRef;
import app.simone.scores.google.ScoreHelper;
import app.simone.shared.application.App;
import app.simone.shared.messages.IMessage;
import app.simone.shared.utils.AnimationHandler;
import app.simone.shared.utils.AudioManager;
import app.simone.shared.utils.Constants;
import app.simone.shared.utils.Utilities;
import app.simone.singleplayer.messages.AttachPresenterMsg;
import app.simone.singleplayer.messages.GuessColorMsg;
import app.simone.singleplayer.messages.NextColorMsg;
import app.simone.singleplayer.messages.PauseMsg;
import app.simone.singleplayer.model.MessageWrapper;
import app.simone.singleplayer.model.SimonColorImpl;
import app.simone.singleplayer.view.GameActivity;

/**
 * Created by nicola on 28/08/2017.
 */

public class GameActivityPresenter {

    private GameActivity activity;
    private int currentScore;
    protected int finalScore;
    private int chosenMode = Constants.CLASSIC_MODE;
    protected boolean playerBlinking;

    protected boolean tapToBegin = true;
    private boolean viewPaused;

    private ActorRef gameViewActor;
    private ActorRef cpuActor;

    public GameActivityPresenter(GameActivity activity, int mode) {
        this.activity = activity;
        this.chosenMode = mode;
        this.gameViewActor = Utilities.getActor(Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().getActorSystem());
        this.cpuActor = Utilities.getActor(Constants.CPU_ACTOR_NAME, App.getInstance().getActorSystem());
        this.gameViewActor.tell(new AttachPresenterMsg(this), ActorRef.noSender());
    }

    /**
     * Handler used to communicate with the GameviewActor. It is the only interface that receives info from outside the class.
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {

            switch (msg.what) {
                case Constants.CPU_TURN:
                    computeCpuTurn(msg);
                    break;
                case Constants.PLAYER_TURN:
                    computePlayerTurn(msg);
                    break;
                case Constants.WHATTASHAMEYOULOST_MSG:
                    handleYouLost(msg);
                    break;
                case Constants.MULTIPLAYER_READY:
                    activity.prepareMultiplayer();
                    break;
            }

        }
    };


    private void computePlayerTurn(Message msg) {
        if (!playerBlinking) {
            activity.updateSimoneTextview(Constants.TURN_PLAYER, AnimationHandler.getGameButtonAnimation());
            if (chosenMode == Constants.HARD_MODE) {
                activity.swapButtonPositions();
            }
        }
        playerBlinking = true;

        // arg1 = 0 when PLAYER_TURN_MSG comes from GameViewActor
        if (msg.arg1 != 0) {
            activity.blinkDelayed(msg);
        }
    }


    private void computeCpuTurn(Message msg) {

        currentScore = msg.arg2 + 1; //Score

        if (playerBlinking) {
            activity.updateSimoneTextview(String.valueOf(currentScore), AnimationHandler.getGameButtonAnimation());
            finalScore = currentScore;
            ScoreHelper.checkAchievement(currentScore, chosenMode);
        }

        playerBlinking = false;

        //arg1 = 0 when PLAYER_TURN_MSG comes from GameViewActor
        if (msg.arg1 != 0) {
            activity.blinkDelayed(msg);
        }
    }


    /**
     * Method that returns the handler used to communicate with the GameviewActor
     * @return handler
     */
    public Handler getHandler() {
        return this.handler;
    }

    private void handleYouLost(Message msg) {
        finalScore = msg.arg1;
        ScoreHelper.sendResultToLeaderboard(chosenMode, finalScore);
        ScoreHelper.checkNGamesAchievement();
        tapToBegin = true;
        activity.saveScore();
        activity.renderYouLost(finalScore);
    }

    public void handleMessage(Message msg) {
        Message m = MessageWrapper.withArg1(msg.what, msg.arg1);
        vHandler.sendMessageDelayed(m, Constants.STD_DELAY_BTN_TIME);
    }

    public void didTapColorButton(int buttonId) {
        if (playerBlinking && !tapToBegin) {
            Message m = MessageWrapper.withArg1(Constants.PLAYER_TURN, buttonId);
            handler.sendMessage(m);
        }
    }

    /**
     * Private handler used to blink
     */
    private Handler vHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            activity.resetButton(msg.arg1);
            switch (msg.what) {
                case Constants.CPU_TURN:
                    gameViewActor.tell(new NextColorMsg(), ActorRef.noSender());
                    break;
                case Constants.PLAYER_TURN:
                    gameViewActor.tell(new GuessColorMsg(SimonColorImpl.fromInt(msg.arg1)), ActorRef.noSender());
                    break;
            }
        }
    };

    public void handleOnResume() {
        if (!playerBlinking && viewPaused) {
            viewPaused = false;
            gameViewActor.tell(new PauseMsg(false), ActorRef.noSender());
        }
    }

    public void handleOnPause() {
        if (!playerBlinking) {
            viewPaused = true;
            gameViewActor.tell(new PauseMsg(true), ActorRef.noSender());
        }
    }

    public void endGame() {
        ScoreHelper.sendResultToLeaderboard(chosenMode, finalScore);
        AudioManager.Companion.getInstance().playSimoneMusic();
    }

    public boolean isTapToBegin() {
        return tapToBegin;
    }

    public void prepareGame(IMessage message) {
        tapToBegin = false;
        finalScore = 0;
        playerBlinking = false;
        cpuActor.tell(message, ActorRef.noSender());
    }

    /**
     * Method that returns true if it's player's turn.
     * @return playerBlinking
     */
    public boolean isPlayerBlinking() {
        return playerBlinking;
    }

    public int getFinalScore() {
        return finalScore;
    }

}
