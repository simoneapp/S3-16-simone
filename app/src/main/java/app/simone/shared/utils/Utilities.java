package app.simone.shared.utils;

import android.app.Activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * @author Michele Sapignoli
 */

public class Utilities {
    /**
     * Returns an ActorRef from the actor's name and the actor system.
     * @param name
     * @param system
     * @return actorRef
     */
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

    public static String stringFromTimestamp(long s){

        long timeStamp = s;

        try{
            DateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }


}
