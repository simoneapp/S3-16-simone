package app.simone.singleplayer.lifeCycleHandler;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import app.simone.shared.utils.AudioManager;

/**
 * Created by Giacomo on 22/01/2018.
 */

public class ApplicationLifeCycleHandler implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    private static final String TAG = ApplicationLifeCycleHandler.class.getSimpleName();
    private static boolean isInBackground = false;
    private Context context;

    public ApplicationLifeCycleHandler(Context c) {
        this.context=c;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {

        if(isInBackground){
            Log.d(TAG, "app went to foreground");
            isInBackground = false;

            SharedPreferences pref = context.getSharedPreferences("MusicPref", 0);
            String s=pref.getString("STRING",null);
            if(s.equals("true")){
                AudioManager.Companion.getInstance().playSimoneMusic();
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override
    public void onLowMemory() {
    }

    @Override
    public void onTrimMemory(int i) {
        if(i == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN){
            Log.d(TAG, "app went to background");
            isInBackground = true;
        }
    }
}
