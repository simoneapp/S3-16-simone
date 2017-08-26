package app.simone.shared.application;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import akka.actor.ActorSystem;
import akka.actor.Props;
import app.simone.multiplayer.controller.FacebookManagerActor;
import app.simone.multiplayer.controller.FacebookViewActor;
import app.simone.scores.google.GoogleApiHelper;
import app.simone.shared.utils.Constants;
import app.simone.singleplayer.controller.CPUActor;
import app.simone.singleplayer.controller.GameViewActor;


/**
 * @author Michele Sapignoli
 */

public class App extends Application {
    private ActorSystem system;
    private GoogleApiHelper googleApiHelper;
    private static App mInstance;

    public static List<ActorDefinitor> actorDefinitions =  Arrays.asList(
            new ActorDefinitor(CPUActor.class, Constants.CPU_ACTOR_NAME),
            new ActorDefinitor(GameViewActor.class, Constants.GAMEVIEW_ACTOR_NAME),
            new ActorDefinitor(FacebookViewActor.class, Constants.FBVIEW_ACTOR_NAME),
            new ActorDefinitor(FacebookManagerActor.class, Constants.FACEBOOK_ACTOR_NAME));


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        googleApiHelper = new GoogleApiHelper(mInstance);
        this.system = buildActorSystem();
    }

    public static ActorSystem buildActorSystem() {
        ActorSystem system = ActorSystem.create("system");

        for(ActorDefinitor definitor : actorDefinitions) {
            system.actorOf(Props.create(definitor.getActorClass()), definitor.getActorName());
        }

        return system;
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
