package app.simone.singleplayer.view;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import akka.actor.ActorRef;
import app.simone.R;
import app.simone.scores.google.ScoreHelper;
import app.simone.shared.application.App;
import app.simone.shared.main.FullscreenBaseGameActivity;
import app.simone.shared.styleable.SimoneTextView;
import app.simone.shared.utils.AnimationHandler;
import app.simone.shared.utils.AudioManager;
import app.simone.shared.utils.AudioPlayer;
import app.simone.shared.utils.Constants;
import app.simone.shared.utils.Utilities;
import app.simone.singleplayer.messages.AttachViewMsg;
import app.simone.singleplayer.messages.GuessColorMsg;
import app.simone.singleplayer.messages.NextColorMsg;
import app.simone.singleplayer.messages.PauseMsg;
import app.simone.singleplayer.messages.StartGameVsCPUMsg;
import app.simone.singleplayer.model.SColor;

/**
 * GameActivity class.
 * Made abstract, so that the abstract methods are implemented in the subclasses as a template method.
 * This class is the class that interacts with the GameviewActor (through an Handler) and the user.
 * @author Michele Sapignoli
 **/
public abstract class GameActivity extends FullscreenBaseGameActivity implements IGameActivity {
    protected boolean playerBlinking;
    protected boolean tapToBegin = true;

    protected FloatingActionButton gameFab;
    protected SimoneTextView simoneTextView;
    protected FloatingActionButton scoreButton;
    protected SimoneTextView scoreText;

    private boolean viewPaused;
    private int chosenMode = Constants.CLASSIC_MODE;

    private List<Button> buttons;
    private Integer[] shuffle = new Integer[]{0, 1, 2, 3};
    private FrameLayout[] layouts = new FrameLayout[4];

    private int currentScore;
    protected int finalScore;
    protected String key;
    protected String whichPlayer;

    /**
     * Handler used to communicate with the GameviewActor. It is the only interface that receives info from outside the class.
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {

            switch (msg.what) {
                case Constants.CPU_TURN:
                    currentScore = msg.arg2 + 1;/*Score*/

                    if (playerBlinking) {
                        simoneTextView.setText(String.valueOf(currentScore));
                        finalScore = currentScore;
                        simoneTextView.startAnimation(AnimationHandler.getGameButtonAnimation());
                        ScoreHelper.checkAchievement(currentScore, chosenMode);
                    }
                    playerBlinking = false;
                    if (/*arg1 = 0 when PLAYER_TURN_MSG comes from GameViewActor*/msg.arg1 != 0) {
                        blinkDelayed(msg);
                    }
                    break;
                case Constants.PLAYER_TURN:
                    if (!playerBlinking) {
                        simoneTextView.setText(Constants.TURN_PLAYER);
                        simoneTextView.startAnimation(AnimationHandler.getGameButtonAnimation());
                        if (chosenMode == Constants.HARD_MODE) {
                            swapButtonPositions();
                        }
                    }
                    playerBlinking = true;
                    if (/*arg1 = 0 when PLAYER_TURN_MSG comes from GameViewActor*/msg.arg1 != 0) {
                        blinkDelayed(msg);
                    }
                    break;
                case Constants.WHATTASHAMEYOULOST_MSG:
                    gameFab.setEnabled(true);
                    finalScore = msg.arg1;
                    ScoreHelper.sendResultToLeaderboard(chosenMode, finalScore);
                    ScoreHelper.checkNGamesAchievement();
                    tapToBegin = true;
                    simoneTextView.startAnimation(AnimationHandler.getGameButtonAnimation());
                    scoreText.setText(""+finalScore);
                    gameFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#990000")));
                    saveScore();
                    scoreText.setVisibility(View.VISIBLE);
                    scoreButton.setVisibility(View.VISIBLE);

                    break;
                case Constants.MULTIPLAYER_READY:
                    gameFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AudioManager.Companion.getInstance().stopSimoneMusic();

                            if (tapToBegin) {
                                gameFab.setEnabled(false);
                                scoreText.setVisibility(View.GONE);
                                scoreButton.setVisibility(View.GONE);
                                tapToBegin = false;
                                finalScore = 0;
                                playerBlinking = false;
                                simoneTextView.startAnimation(AnimationHandler.getGameButtonAnimation());
                                simoneTextView.setText(Constants.STRING_EMPTY);
                                simoneTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#737373")));
                                gameFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f2f2f2")));

                                Utilities.getActorByName(Constants.PATH_ACTOR + Constants.CPU_ACTOR_NAME, App.getInstance().getActorSystem())
                                        .tell(new StartGameVsCPUMsg(), ActorRef.noSender());
                            }
                        }
                    });
                    break;
            }

        }
    };

    private void blinkDelayed(Message msg) {
        // Message from ViewActor or this activity itself, handling the blinking
        Button b = (Button) findViewById(msg.arg1);
        b.setAlpha(0.4f);
        new AudioPlayer().play(getApplicationContext(), SColor.fromInt(msg.arg1).getSoundId());
        Message m = new Message();
        m.what = msg.what;
        m.arg1 = msg.arg1;
        vHandler.sendMessageDelayed(m, Constants.STD_DELAY_BTN_TIME);

    }

    /**
     * Private handler used to blink
     */
    private Handler vHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Button b = (Button) findViewById(msg.arg1);
            b.setAlpha(1);

            switch (msg.what) {
                case Constants.CPU_TURN:
                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().getActorSystem())
                            .tell(new NextColorMsg(), ActorRef.noSender());
                    break;
                case Constants.PLAYER_TURN:
                    Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().getActorSystem())
                            .tell(new GuessColorMsg(SColor.fromInt(msg.arg1)), ActorRef.noSender());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chosenMode = getIntent().getIntExtra(Constants.CHOSEN_MODE, Constants.CLASSIC_MODE);
        buttons = new ArrayList<>();
        gameFab = (FloatingActionButton) findViewById(R.id.game_fab);
        simoneTextView = (SimoneTextView) findViewById(R.id.game_simone_textview);
        scoreText =(SimoneTextView) findViewById(R.id.game_score_textview);
        scoreButton = (FloatingActionButton) findViewById(R.id.game_score_fab);

        AnimationHandler.initAnimations(this, gameFab, simoneTextView);

        layouts[0] = (FrameLayout) findViewById(R.id.game_top_left_frame);
        layouts[1] = (FrameLayout) findViewById(R.id.game_top_right_frame);
        layouts[2] = (FrameLayout) findViewById(R.id.game_bottom_left_frame);
        layouts[3] = (FrameLayout) findViewById(R.id.game_bottom_right_frame);

        Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().getActorSystem())
                .tell(new AttachViewMsg(this), ActorRef.noSender());

        this.setup();
    }

    /**
     * Abstract method implemented in the subclasses that sets up the settings of the SingleplayerGameActivity or the MultiplayerGameActivity
     */
    abstract void setup();

    /**
     * Abstract method implemented in the subclasses that handles the score
     */
    abstract void saveScore();

    /**
     * Implementation of the abstract method of FullscreenBaseGameActivity, used to set the contentView.
     */
    @Override
    protected void setSubclassContentView() {
        setContentView(R.layout.activity_game);
        mContentView = findViewById(R.id.game_fullscreen_content);
    }

    /**
     * Method that returns the handler used to communicate with the GameviewActor
     * @return handler
     */
    public Handler getHandler() {
        return this.handler;
    }

    /**
     * Method that returns true if it's player's turn.
     * @return playerBlinking
     */
    public boolean isPlayerBlinking() {
        return this.playerBlinking;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!playerBlinking) {
            this.viewPaused = true;
            Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().getActorSystem())
                    .tell(new PauseMsg(true), ActorRef.noSender());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!playerBlinking && viewPaused) {
            this.viewPaused = false;
            Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().getActorSystem())
                    .tell(new PauseMsg(false), ActorRef.noSender());
        }
    }

    @Override
    protected void backTransition() {
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    private void swapButtonPositions() {
        Collections.shuffle(Arrays.asList(shuffle));
        for (FrameLayout f : layouts) {
            f.removeAllViews();
        }
        AnimationHandler.performColorSwapAnimation(this, this.shuffle, this.buttons, this.layouts);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (buttons.size() == 0) {
            buttons.add(initColorButton(SColor.GREEN));
            buttons.add(initColorButton(SColor.RED));
            buttons.add(initColorButton(SColor.YELLOW));
            buttons.add(initColorButton(SColor.BLUE));
        }
    }


    /**
     * Initialization of the buttons, with relative listener.
     * @param color
     * @return button
     */
    private Button initColorButton(final SColor color) {
        final Button button = (Button) findViewById(color.getButtonId());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerBlinking && !tapToBegin) {
                    Message m = new Message();
                    m.arg1 = color.getButtonId();
                    m.what = Constants.PLAYER_TURN;
                    handler.sendMessage(m);
                }
            }
        });
        return button;
    }

    @Override
    public void onBackPressed() {

        if (!tapToBegin) {
            AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
            alertDialog.setTitle("Are you letting Simone win?");
            alertDialog.setMessage("Your final score will be considered " + finalScore);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            saveScore();
                            finish();
                            ScoreHelper.sendResultToLeaderboard(chosenMode, finalScore);
                            GameActivity.super.onBackPressed();
                            AudioManager.Companion.getInstance().playSimoneMusic();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } else {
            super.onBackPressed();
        }
    }
}
