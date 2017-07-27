package app.simone.shared.application;

import android.app.Application;

import com.facebook.stetho.Stetho;

import app.simone.singleplayer.controller.CPUActor;
import app.simone.singleplayer.controller.GameViewActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import app.simone.multiplayer.controller.FacebookManagerActor;
import app.simone.multiplayer.controller.FacebookViewActor;
import app.simone.shared.utils.Constants;
import app.simone.scores.google.GoogleApiHelper;

import static android.R.attr.key;

/**
 * @author Michele Sapignoli
 */

public class App extends Application {
    private ActorSystem system;
    private GoogleApiHelper googleApiHelper;
    private static App mInstance;


    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
        mInstance = this;
        googleApiHelper = new GoogleApiHelper(mInstance);

        system = ActorSystem.create("system");
        system.actorOf(Props.create(CPUActor.class), Constants.CPU_ACTOR_NAME);
        system.actorOf(Props.create(GameViewActor.class), Constants.GAMEVIEW_ACTOR_NAME);
        system.actorOf(Props.create(FacebookViewActor.class), Constants.FBVIEW_ACTOR_NAME);
        system.actorOf(Props.create(FacebookManagerActor.class), Constants.FACEBOOK_ACTOR_NAME);
    }

    public ActorSystem getActorSystem(){
        return system;
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    public GoogleApiHelper getGoogleApiHelperInstance() {
        return this.googleApiHelper;
    }
    public static GoogleApiHelper getGoogleApiHelper() {
        return getInstance().getGoogleApiHelperInstance();
    }

}
