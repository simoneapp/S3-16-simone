package application;

import android.app.Application;

import actors.CPUActor;
import actors.GameViewActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import app.simone.users.FacebookManagerActor;
import app.simone.users.FacebookViewActor;
import utils.Constants;
import utils.google.GoogleApiHelper;

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
