package PubNub;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import app.simone.R;
import app.simone.users.FacebookLoginActivity;

/**
 * Created by Giacomo on 06/07/2017.
 */

public class PushNotification {

    private String name;
    private Context myContext;

    public PushNotification(Context c,String s){
        myContext=c;
        name = s;
    }

    public void init(){
        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(myContext)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Simone app")
                        .setContentText(this.name +" vuole giocare con te!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(myContext, FacebookLoginActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(myContext);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(FacebookLoginActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager)myContext.getSystemService(Context.NOTIFICATION_SERVICE);
    // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }
}
