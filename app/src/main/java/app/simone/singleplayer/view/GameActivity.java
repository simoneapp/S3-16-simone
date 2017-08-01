package app.simone.singleplayer.view;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.*;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import java.util.*;
import app.simone.multiplayer.controller.*;
import app.simone.multiplayer.model.*;
import akka.actor.ActorRef;
import app.simone.shared.main.FullscreenBaseGameActivity;
import app.simone.R;
import app.simone.singleplayer.model.SColor;
import app.simone.shared.styleable.SimoneTextView;
import app.simone.multiplayer.model.FacebookUser;
import app.simone.shared.application.App;
import app.simone.singleplayer.messages.*;
import app.simone.shared.utils.*;
import app.simone.scores.google.ScoreHelper;

/**
 * @author Michele Sapignoli
 */
public class GameActivity extends FullscreenBaseGameActivity implements IGameActivity {
    private boolean playerBlinking;
    private boolean tapToBegin = true;

    private FloatingActionButton gameFab;
    private MessageHandler msgHandler;
    private boolean isMultiplayerMode = false;
    private SimoneTextView simoneTextView;

    private boolean viewPaused;

    private int chosenMode = Constants.CLASSIC_MODE;

    private List<Button> buttons;
    private Integer[] shuffle = new Integer[]{0, 1, 2, 3};

    private FrameLayout[] layouts = new FrameLayout[4];

    private FacebookUser sender;
    private FacebookUser recipient;
    private boolean isGameEnded = false;
    private String whichPlayer = "";

    private int currentScore;
    private int finalScore;
    private int opponentScore = -1;

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
                    finalScore = msg.arg1;
                    ScoreHelper.sendResultToLeaderboard(chosenMode, finalScore);
                    tapToBegin = true;
                    simoneTextView.setText(Constants.PLAY_AGAIN);
                    simoneTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                    gameFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#990000")));
                    sendMsgToOtherPlayer();
                    simoneTextView.startAnimation(AnimationHandler.getGameButtonAnimation());
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
        DataManager.Companion.getInstance().setup(this);

        String senderID = (String) getIntent().getExtras().getSerializable("sender");
        String recipientID = (String) getIntent().getExtras().getSerializable("recipient");

        try {
            checkingExistingData(senderID, recipientID);
        } catch (Exception e) {
            System.out.println("error while retrieving data from DB");
        }

        //Giaki
        if (sender != null && !getIntent().hasExtra("multiplayerMode")) {
            whichPlayer = "p1";
            isMultiplayerMode = true;
            msgHandler = new MessageHandler(new PubnubController("multiplayer"));
            Request request = new Request(sender, recipient);
            try {
                OnlineMatch newMatch = msgHandler.createMatch(request);
                newMatch.setKindOfMsg("insert");
                newMatch.getFirstPlayer().setScore("");
                msgHandler.publishMessage(newMatch);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("GameActivity", "Error while publishing the message on the channel");
            }


        } else if (getIntent().hasExtra("multiplayerMode")) {
            // This code is executed by P2
            checkingExistingData(senderID, recipientID);
            whichPlayer = "p2";
            isMultiplayerMode = true;
            msgHandler = new MessageHandler(new PubnubController("multiplayer"));
            try {
                opponentScore = Integer.parseInt(getIntent().getExtras().getString("temporaryScore"));
                Log.d("OPPONENT SCORE",""+opponentScore);
            }catch (Exception e){
                System.out.println("Opponent score not available yet");
            }
        }

        chosenMode = getIntent().getIntExtra(Constants.CHOSEN_MODE, Constants.CLASSIC_MODE);

        buttons = new ArrayList<>();

        gameFab = (FloatingActionButton) findViewById(R.id.game_fab);
        simoneTextView = (SimoneTextView) findViewById(R.id.game_simone_textview);

        AnimationHandler.initAnimations(this, gameFab, simoneTextView);

        layouts[0] = (FrameLayout) findViewById(R.id.game_top_left_frame);
        layouts[1] = (FrameLayout) findViewById(R.id.game_top_right_frame);
        layouts[2] = (FrameLayout) findViewById(R.id.game_bottom_left_frame);
        layouts[3] = (FrameLayout) findViewById(R.id.game_bottom_right_frame);

        gameFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AudioManager.Companion.getInstance().stopSimoneMusic();

                if (tapToBegin) {
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


        Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().getActorSystem())
                .tell(new AttachViewMsg(this), ActorRef.noSender());
    }

    @Override
    protected void setSubclassContentView() {
        setContentView(R.layout.activity_game);
        mContentView = findViewById(R.id.game_fullscreen_content);
    }


    public Handler getHandler() {
        return this.handler;
    }

    public boolean isPlayerBlinking() {
        return this.playerBlinking;
    }

    public void setPlayerBlinking(boolean isPlayerTurn) {
        this.playerBlinking = isPlayerTurn;
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

    private void sendMsgToOtherPlayer() {
        if (isMultiplayerMode) {
            isGameEnded = true;
            simoneTextView.setText(Constants.BACK_TO_MENU);
            simoneTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            if (whichPlayer == "p1") {
                sender.setScore("" + finalScore);
                Request request = new Request(sender, recipient);
                OnlineMatch newMatch = msgHandler.createMatch(request);
                msgHandler.setMessageFields(newMatch);
                msgHandler.publishMessage(newMatch);
            } else if (whichPlayer == "p2") {
                updateOpponentScore();
                recipient.setScore("" + finalScore);
                Request request = new Request(sender, recipient);
                OnlineMatch newMatch = msgHandler.createMatch(request);
                newMatch.setKindOfMsg("update");
                msgHandler.publishMessage(newMatch);

            }

        }
    }

    private void updateOpponentScore(){
        if(opponentScore!=-1) {
            sender.setScore("" + opponentScore);
        }
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
                            if (isMultiplayerMode) {
                                sendMsgToOtherPlayer();
                            }
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

    private void checkingExistingData(String senderID, String recipientID) {

        sender = ScoreHandler.checkingExistingUser(senderID);
        recipient = ScoreHandler.checkingExistingUser(recipientID);

    }

}
