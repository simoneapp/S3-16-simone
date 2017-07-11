package app.simone.shared.main;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import app.simone.shared.application.App;
import app.simone.shared.utils.Constants;
import app.simone.singleplayer.view.GameActivity;

/**
 * @author Michele Sapignoli
 */

public abstract class FullscreenBaseGameActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    protected int UI_ANIMATION_DELAY = 300;
    protected int initialDelay = 100;
    protected boolean mVisible;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    protected View mContentView;
    protected Handler googleHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.CONNECT:
                    if (App.getGoogleApiHelper().getGoogleApiClient() == null || !App.getGoogleApiHelper().getGoogleApiClient().isConnected()) {
                        App.getGoogleApiHelper().buildGoogleApiClient((FullscreenBaseGameActivity)msg.obj, mContentView);
                    }
                    break;
                case Constants.CONNECTION_ERROR:
                    showConnectionErrorDialog();
                    break;
            }

        }
    };

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            //mControlsView.setVisibility(View.VISIBLE);
        }
    };

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSubclassContentView();

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
    }

    protected abstract void setSubclassContentView();

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        //delayedHide(initialDelay);


    }


    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    protected void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }


    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }


    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    public void openActivity(Class activity, int enterAnim, int exitAnim) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        overridePendingTransition(enterAnim, exitAnim);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backTransition();
    }

    public void openActivity(Class activity, String extraName, int extra, int enterAnim, int exitAnim) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(extraName, extra);
        startActivity(intent);
        overridePendingTransition(enterAnim, exitAnim);
    }

    protected abstract void backTransition();

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("##FULLSCREEN ACTIVITY", "connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("##FULLSCREEN ACTITIVY", "onConnectionSuspended: googleApiClient.connect()");
        this.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("##FULLSCREEN ACTIVITY", "onConnectionFailed: connectionResult.toString() = " + connectionResult.toString());
        if (connectionResult.hasResolution()) {
            try {
                // !!!
                connectionResult.startResolutionForResult(this, 1);
            } catch (IntentSender.SendIntentException e) {
                this.connect();
            }
        }
        googleHandler.sendEmptyMessageDelayed(Constants.CONNECTION_ERROR,500);

    }

    public void connect() {
        if (App.getGoogleApiHelper().getGoogleApiClient() != null) {
            App.getGoogleApiHelper().getGoogleApiClient().connect();
        }
    }

    private void showConnectionErrorDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Connection error");
        alertDialog.setMessage("Simone was not able to reach Google Games. Enable connection to improve your experience with Simone.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}
