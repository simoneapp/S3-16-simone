package app.simone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import app.simone.users.GoogleGamesActivity;
import io.fabric.sdk.android.Fabric;

import app.simone.users.FacebookLoginActivity;

/**
 * @author Michele Sapignoli
 */
public class MainActivity extends FullscreenActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private Button VSCpuButton;
    private Button connectionButton;
    private Button btnMultiplayer;
    private Button multiplayerButton;
    final Handler handler = new Handler();
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Games.API)
                 .addConnectionCallbacks(this)
                .addScope(Games.SCOPE_GAMES)
                .build();


        Fabric.with(this, new Crashlytics());

        VSCpuButton = (Button)findViewById(R.id.button_vs_cpu);
        connectionButton = (Button)findViewById(R.id.button4);
        btnMultiplayer = (Button)findViewById(R.id.main_button_multiplayer);


        //Listener on vs CPUActor button
        VSCpuButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openActivity(VSCpuActivity.class);
            }
        });

        btnMultiplayer.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openActivity(FacebookLoginActivity.class);
            }
        });

        connectionButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openActivity(SettingsActivity.class);
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);


        multiplayerButton = (Button) findViewById(R.id.main_button_multiplayer);
        multiplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(FacebookLoginActivity.class);
            }
        });

        FloatingActionButton mainFab = (FloatingActionButton) findViewById(R.id.main_fab);
        TextView simoneTextView = (TextView) findViewById(R.id.main_simone_textview);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.pulse);
        mainFab.startAnimation(animation);
        simoneTextView.startAnimation(animation);

        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GoogleGamesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        //delayedHide(100);
    }

    @Override
    protected void backTransition() {
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    protected void forwardTransition() {
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    protected void setSubclassContentView() {
        setContentView(R.layout.activity_main);
        mContentView = findViewById(R.id.main_fullscreen_content);
        mVisible = true;
    }
    public void playVsCpu(View view){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("##MAIN ACTIVITY", "Connection failed");
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
