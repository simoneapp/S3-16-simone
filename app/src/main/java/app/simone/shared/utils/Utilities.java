package app.simone.shared.utils;

import android.app.Activity;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Identify;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

/**
 * Created by sapi9 on 20/06/2017.
 */

public class Utilities {
    public static ActorRef getActorByName(String name, ActorSystem system){
        ActorSelection sel = system.actorSelection(name);

        Timeout t = new Timeout(3, TimeUnit.SECONDS);
        AskableActorSelection asker = new AskableActorSelection(sel);
        Future<Object> fut = asker.ask(new Identify(1), t);
        ActorIdentity ident = null;
        try {
            ident = (ActorIdentity) Await.result(fut, t.duration());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ident.getRef();
    }


    public static void displayToast(final String text, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                android.widget.Toast.makeText(activity, text, android.widget.Toast.LENGTH_LONG).show();
            }
        });
    }
}
