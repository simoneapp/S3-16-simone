package app.simone.singleplayer.view;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import app.simone.R;
import app.simone.shared.main.FullscreenBaseGameActivity;
import app.simone.shared.styleable.SimoneTextView;
import app.simone.shared.utils.AnimationHandler;
import app.simone.shared.utils.AudioManager;
import app.simone.shared.utils.AudioPlayer;
import app.simone.shared.utils.Constants;
import app.simone.singleplayer.controller.GameActivityPresenter;
import app.simone.singleplayer.messages.StartGameVsCPUMsg;
import app.simone.singleplayer.model.SimonColorImpl;

/**
 * GameActivity class.
 * Made abstract, so that the abstract methods are implemented in the subclasses as a template method.
 * This class is the class that interacts with the GameviewActor (through an Handler) and the user.
 * @author Michele Sapignoli
 **/
public abstract class GameActivity extends FullscreenBaseGameActivity {

    protected FloatingActionButton gameFab;
    protected SimoneTextView simoneTextView;
    protected FloatingActionButton scoreButton;
    protected SimoneTextView scoreText;

    private List<Button> buttons;
    private Integer[] shuffle = new Integer[]{0, 1, 2, 3};
    private FrameLayout[] layouts = new FrameLayout[4];

    protected String key;
    protected String whichPlayer;

    protected GameActivityPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int mode = getIntent().getIntExtra(Constants.CHOSEN_MODE, Constants.CLASSIC_MODE);
        presenter = new GameActivityPresenter(this, mode);
        initFields();

        AnimationHandler.initAnimations(this, gameFab, simoneTextView);

        int[] viewIds = new int[]{R.id.game_top_left_frame, R.id.game_top_right_frame,
                R.id.game_bottom_left_frame, R.id.game_bottom_right_frame};

        for(int i = 0; i < layouts.length; i++) {
            layouts[i] = (FrameLayout)findViewById(viewIds[i]);
        }

        this.setup();
    }

    private void initFields() {
        buttons = new ArrayList<>();
        gameFab = (FloatingActionButton) findViewById(R.id.game_fab);
        simoneTextView = (SimoneTextView) findViewById(R.id.game_simone_textview);
        scoreText =(SimoneTextView) findViewById(R.id.game_score_textview);
        scoreButton = (FloatingActionButton) findViewById(R.id.game_score_fab);
    }

    public void renderYouLost(int score) {
        gameFab.setEnabled(true);
        simoneTextView.startAnimation(AnimationHandler.getGameButtonAnimation());
        scoreText.setText(""+score);
        gameFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#990000")));
        scoreText.setVisibility(View.VISIBLE);
        scoreButton.setVisibility(View.VISIBLE);
    }

    public void updateSimoneTextview(String text, Animation animation) {
        simoneTextView.setText(text);
        simoneTextView.startAnimation(animation);
    }

    public void blinkDelayed(Message msg) {
        Button b = (Button) findViewById(msg.arg1);
        b.setAlpha(0.4f);
        new AudioPlayer().play(getApplicationContext(), SimonColorImpl.fromInt(msg.arg1).getSoundId());
        presenter.handleMessage(msg);
    }

    public void resetButton(int buttonId) {
        Button b = (Button)findViewById(buttonId);
        b.setAlpha(1);
    }

    public void prepareMultiplayer(){
        gameFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AudioManager.Companion.getInstance().stopSimoneMusic();

                if (presenter.isTapToBegin()) {
                    prepareViewsForGame();
                    presenter.prepareGame(new StartGameVsCPUMsg());
                }
            }
        });
    }

    protected void prepareViewsForGame() {
        scoreText.setVisibility(View.GONE);
        scoreButton.setVisibility(View.GONE);
        gameFab.setEnabled(false);
        simoneTextView.startAnimation(AnimationHandler.getGameButtonAnimation());
        simoneTextView.setText(Constants.STRING_EMPTY);
        simoneTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#737373")));
        gameFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f2f2f2")));
    }

    /**
     * Abstract method implemented in the subclasses that sets up the settings of the
     * SingleplayerGameActivity or the MultiplayerGameActivity
     */
    abstract void setup();

    /**
     * Abstract method implemented in the subclasses that handles the score
     */
    public abstract void saveScore();

    /**
     * Implementation of the abstract method of FullscreenBaseGameActivity, used to set the
     * contentView.
     */
    @Override
    protected void setSubclassContentView() {
        setContentView(R.layout.activity_game);
        mContentView = findViewById(R.id.game_fullscreen_content);
    }

    /**
     * Convenient method that returns true if it's player's turn.
     * @return playerBlinking
     */
    public boolean isPlayerBlinking() {
        return this.presenter.isPlayerBlinking();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.handleOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.handleOnResume();
    }

    @Override
    protected void backTransition() {
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    public void swapButtonPositions() {
        Collections.shuffle(Arrays.asList(shuffle));
        for (FrameLayout f : layouts) {
            f.removeAllViews();
        }
        AnimationHandler.performColorSwapAnimation(this, shuffle, buttons, layouts);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (buttons.size() == 0) {
            SimonColorImpl[] colors = new SimonColorImpl[]{
                    SimonColorImpl.GREEN, SimonColorImpl.RED, SimonColorImpl.YELLOW, SimonColorImpl.BLUE};
            for(SimonColorImpl color : colors) {
                buttons.add(initColorButton(color));
            }
        }
    }

    /**
     * Initialization of the buttons, with relative listener.
     * @param color
     * @return button
     */
    private Button initColorButton(final SimonColorImpl color) {
        final Button button = (Button) findViewById(color.getButtonId());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.didTapColorButton(color.getButtonId());
            }
        });
        return button;
    }

    @Override
    public void onBackPressed() {

        if (!presenter.isTapToBegin()) {
            AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
            alertDialog.setTitle("Are you letting Simone win?");
            alertDialog.setMessage("Your final score will be considered " + presenter.getFinalScore());
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            saveScore();
                            finish();
                            presenter.endGame();
                            GameActivity.super.onBackPressed();
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