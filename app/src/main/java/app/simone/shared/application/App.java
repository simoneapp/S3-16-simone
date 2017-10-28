package app.simone.shared.application;

import android.app.Application;

import java.util.Arrays;
import java.util.List;
import akka.actor.ActorSystem;
import akka.actor.Props;
import app.simone.scores.google.GoogleApiHelper;
import app.simone.shared.utils.Analytics;
import app.simone.shared.utils.Constants;


/** App class, extends Application.
 *
 * @author Michele Sapignoli
 */

public class App extends Application {
    private ActorSystem system;
    private GoogleApiHelper googleApiHelper;
    private static App mInstance;

    public static List<ActorDefinitor> actorDefinitions =  Arrays.asList(
            new ActorDefinitor("app.simone.singleplayer.controller.CPUActor", Constants.CPU_ACTOR_NAME),
            new ActorDefinitor("app.simone.singleplayer.controller.GameViewActor", Constants.GAMEVIEW_ACTOR_NAME),
            new ActorDefinitor("app.simone.multiplayer.controller.FacebookViewActor", Constants.FBVIEW_ACTOR_NAME),
            new ActorDefinitor("app.simone.multiplayer.controller.FacebookManagerActor", Constants.FACEBOOK_ACTOR_NAME));

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        googleApiHelper = new GoogleApiHelper(mInstance);
        Analytics.Companion.logAppOpen(this);
        this.system = buildActorSystem();
    }

    public static ActorSystem buildActorSystem() {
        ActorSystem system = ActorSystem.create("system");

        for(ActorDefinitor definitor : actorDefinitions) {
            try {
                system.actorOf(Props.create(Class.forName(definitor.getActorClass())), definitor.getActorName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return system;
    }

    public ActorSystem getActorSystem(){
        return system;
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    /**
     * Gets GoogleApiHelper instance.
     * @return googleApiHelper
     */
    public GoogleApiHelper getGoogleApiHelperInstance() {
        return this.googleApiHelper;
    }

    /**
     * Gets GoogleApiHelper instance.
     * @return googleApiHelper
     */
    public static GoogleApiHelper getGoogleApiHelper() {
        return getInstance().getGoogleApiHelperInstance();
    }



}
