package app.simone.singleplayer.controller;

import android.os.Handler;
import android.os.Message;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
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
import app.simone.singleplayer.model.MessageBuilder;
import app.simone.singleplayer.model.SimonColorImpl;
import app.simone.singleplayer.view.GameActivity;

/**
 * Bridging class between the Game Activity and the Game View Actor.
 * @author Michele Sapignoli, Nicola Giancecchi
 */
public class GameActivityPresenter {

    private GameActivity activity;
    private int currentScore;
    private int finalScore;
    private int chosenMode = Constants.CLASSIC_MODE;
    private boolean playerBlinking;
    private boolean tapToBegin = true;
    private boolean viewPaused;
    private ActorRef gameViewActor;
    private ActorRef cpuActor;
    private ActorRef sender;


    public GameActivityPresenter() {

    }

    public GameActivityPresenter(GameActivity activity, int mode) {
        setup(activity, mode, App.getInstance().getActorSystem(), ActorRef.noSender());
    }

    public GameActivityPresenter(GameActivity activity, int mode, ActorSystem system, ActorRef sender) {
        setup(activity, mode, system, sender);
    }

    public void setup(GameActivity activity, int mode, ActorSystem system, ActorRef sender) {
        this.activity = activity;
        this.sender = sender;
        this.chosenMode = mode;
        this.gameViewActor = Utilities.getActor(Constants.GAMEVIEW_ACTOR_NAME, system);
        this.cpuActor = Utilities.getActor(Constants.CPU_ACTOR_NAME, system);
        this.gameViewActor.tell(new AttachPresenterMsg(this), sender);
    }

    public GameActivity getActivity() {
        return activity;
    }

    /**
     * Handler used to communicate with the GameviewActor. It is the only interface that receives
     * info from outside the class.
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

    /**
     * Entry point handler for a PLAYER_TURN message
     * @param msg the message received
     */
    private void computePlayerTurn(Message msg) {
        if (!playerBlinking) {
            activity.updateSimoneTextview(Constants.TURN_PLAYER,
                    AnimationHandler.getGameButtonAnimation());
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


    /**
     *  Prepares the game variables and tells the CPU Actor to start.
     * @param message the received IMessage.
     */
    public void prepareGame(IMessage message) {
        tapToBegin = false;
        finalScore = 0;
        playerBlinking = false;
        cpuActor.tell(message, sender);
    }

    /**
     * Entry point handler for a CPU_TURN message
     * @param msg the message received
     */
    private void computeCpuTurn(Message msg) {

        currentScore = msg.arg2 + 1; //Score

        if (playerBlinking) {
            activity.updateSimoneTextview(String.valueOf(currentScore),
                    AnimationHandler.getGameButtonAnimation());
            finalScore = currentScore;
            ScoreHelper.INSTANCE.checkAchievement(activity.getContext(), currentScore, chosenMode);
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

    /**
     * Handler of the "You Lost" message, occurring when a user loses the game
     * @param msg the received message
     */
    private void handleYouLost(Message msg) {
        finalScore = msg.arg1;
        ScoreHelper.INSTANCE.sendResultToLeaderboard(activity.getContext(), chosenMode, finalScore);
        ScoreHelper.INSTANCE.checkNGamesAchievement(activity.getContext());
        tapToBegin = true;
        activity.saveScore();
        activity.renderYouLost(finalScore);
    }

    /**
     * Handler for the blink messages, received from the GameActivity.
     * @param msg the message received
     */
    public void handleBlinkDelayedMessage(Message msg) {
        blinkHandler.sendMessageDelayed(msg, Constants.STD_DELAY_BTN_TIME);
    }

    /**
     * Method called from the game activity and attached to each of the four colored buttons.
     * @param buttonId Resource ID for the button
     */
    public void didTapColorButton(int buttonId) {
        if (playerBlinking && !tapToBegin) {
            Message m = MessageBuilder.withArg1(Constants.PLAYER_TURN, buttonId);
            getHandler().sendMessage(m);
        }
    }

    /**
     * Private handler used to blink
     */
    private Handler blinkHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            activity.resetButton(msg.arg1);
            IMessage message = null;
            switch (msg.what) {
                case Constants.CPU_TURN:
                    message = new NextColorMsg();
                    break;
                case Constants.PLAYER_TURN:
                    message = new GuessColorMsg(SimonColorImpl.fromInt(msg.arg1));
                    break;
            }

            gameViewActor.tell(message, sender);
        }
    };

    /**
     * Bridging methods for OnResume and OnPause
     */
    public void handleOnResume() {
        if (!playerBlinking && viewPaused) {
            viewPaused = false;
            gameViewActor.tell(new PauseMsg(false), sender);
        }
    }

    public void handleOnPause() {
        if (!playerBlinking) {
            viewPaused = true;
            gameViewActor.tell(new PauseMsg(true), sender);
        }
    }

    /**
     * Method called when the user taps on the "back" button and the game ends
     */
    public void endGame() {
        ScoreHelper.INSTANCE.sendResultToLeaderboard(activity.getContext(), chosenMode, finalScore);
        AudioManager.Companion.getInstance().playSimoneMusic();
    }

    void blinkDelayed(Message m, long time) {
        getHandler().sendMessageDelayed(m, time);
    }

    public boolean isTapToBegin() {
        return tapToBegin;
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
