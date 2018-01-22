package app.simone.shared.main;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import app.simone.R;
import app.simone.multiplayer.controller.NearbyGameController;
import app.simone.multiplayer.view.nearby.NearbyGameActivity;
import app.simone.scores.view.ScoreboardActivity;
import app.simone.settings.controller.SettingsManager;
import app.simone.settings.view.SettingsActivity;
import app.simone.shared.firebase.FCMTokenService;
import app.simone.shared.utils.Analytics;
import app.simone.shared.utils.AnalyticsAppAction;
import app.simone.shared.utils.AudioManager;
import app.simone.shared.utils.Constants;
import app.simone.singleplayer.view.ApplicationLifeCycleHandler;
import app.simone.singleplayer.view.HomeWatcher;
import app.simone.singleplayer.view.OnHomePressedListener;
import app.simone.singleplayer.view.VSCpuActivity;
import io.fabric.sdk.android.Fabric;

/**
 * MainActivity of the app.
 *
 * @author Michele Sapignoli
 */
public class MainActivity extends FullscreenBaseGameActivity {

    private Button VSCpuButton;
    private Button connectionButton;
    private Button btnMultiplayer;
    private Button multiplayerButton;
    private SettingsManager settingsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationLifeCycleHandler handler = new ApplicationLifeCycleHandler(getApplicationContext());
        getApplication().registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);

        Fabric.with(this, new Crashlytics());

        VSCpuButton = (Button)findViewById(R.id.button_vs_cpu);
        connectionButton = (Button)findViewById(R.id.button4);
        multiplayerButton = (Button) findViewById(R.id.main_button_multiplayer);

        //Listener on vs CPUActor button
        VSCpuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Analytics.Companion.logAppAction(AnalyticsAppAction.SINGLE_PLAYER_TOUCHED, getApplicationContext());
                openActivity(VSCpuActivity.class, R.anim.left_in, R.anim.right_out);
            }
        });
/*
        multiplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Analytics.Companion.logAppAction(AnalyticsAppAction.MULTI_PLAYER_TOUCHED, getApplicationContext());
                openActivity(MultiplayerTypeActivity.class, R.anim.slide_down, R.anim.slide_down_existing);
            }
        });
*/
        multiplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openActivity(MultiplayerTypeActivity.class, R.anim.slide_down, R.anim.slide_down_existing);
                Analytics.Companion.logAppAction(AnalyticsAppAction.MULTI_PLAYER_TOUCHED, getApplicationContext());
                openActivity(ComingSoonActivity.class, R.anim.slide_down, R.anim.slide_down_existing);
            }
        });
      
        connectionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Analytics.Companion.logAppAction(AnalyticsAppAction.SETTINGS_TOUCHED, getApplicationContext());
                openActivity(SettingsActivity.class, R.anim.right_in, R.anim.left_out);
            }
        });


        FloatingActionButton mainFab = (FloatingActionButton) findViewById(R.id.main_fab);
        TextView simoneTextView = (TextView) findViewById(R.id.main_simone_textview);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.pulse);
        mainFab.startAnimation(animation);
        simoneTextView.startAnimation(animation);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MusicPref", 0);

        try{
            String s=pref.getString("STRING",null);
            if(s==null){
                AudioManager.Companion.getInstance().playSimoneMusic();
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("STRING", "true");
                editor.commit();
            }else if(s.equals("true")){
                AudioManager.Companion.getInstance().playSimoneMusic();
            }
        }catch (Exception e){
            Log.d("PREF","NO preferences!");
        }

        //AudioManager.Companion.getInstance().playSimoneMusic();

        Button b = (Button) findViewById(R.id.main_button_highscore);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Analytics.Companion.logAppAction(AnalyticsAppAction.SCOREBOARD_TOUCHED, getApplicationContext());
               openActivity(ScoreboardActivity.class, R.anim.slide_up, R.anim.slide_up_existing);
            }
        });
        this.watchHome();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        //delayedHide(100);
        Message msg = new Message();
        msg.what = Constants.CONNECT;
        msg.obj = this;
        msg.arg1 = 1;
        googleHandler.sendMessage(msg);
        Message m = new Message();
        m.what = Constants.CONNECT;
        m.obj = this;
        googleHandler.sendMessageDelayed(m,100);

        Intent intent = new Intent(this, FCMTokenService.class);
        startService(intent);
        FCMTokenService.Companion.updateCurrentToken();
        NearbyGameController controller = new NearbyGameController();
        controller.updateUserData();
    }


    @Override
    protected void backTransition() {
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    protected void setSubclassContentView() {
        setContentView(R.layout.activity_main);
        mContentView = findViewById(R.id.main_fullscreen_content);
        mVisible = true;
    }

    public void multiplayerSetUp(View view) {
        Intent intent = new Intent(this, NearbyGameActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


   private void watchHome(){
       HomeWatcher mHomeWatcher = new HomeWatcher(this);
       mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
           @Override
           public void onHomePressed() {
               // do something here...
               Log.d("Home","HomeButton pressed!");
               AudioManager.Companion.getInstance().stopSimoneMusic();
           }
           @Override
           public void onHomeLongPressed() {
           }
       });
       mHomeWatcher.startWatch();
   }





}
