package application;

import android.app.Application;

import actors.CPUActor;
import actors.PlayerActorJava;
import actors.ViewActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import utils.Constants;

/**
 * Created by sapi9 on 21/06/2017.
 */

public class mApplication extends Application {
    private static ActorSystem system;
    @Override
    public void onCreate() {
        super.onCreate();

        system = ActorSystem.create("system");
        system.actorOf(Props.create(PlayerActorJava.class), Constants.PLAYER_ACTOR_NAME);
        system.actorOf(Props.create(CPUActor.class), Constants.CPU_ACTOR_NAME);
        system.actorOf(Props.create(ViewActor.class), Constants.VIEW_ACTOR_NAME);
    }

    public static ActorSystem getActorSystem(){
        return system;
    }
}
