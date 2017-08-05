package app.simone.shared.firebase

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.support.v7.app.NotificationCompat
import android.util.Log
import app.simone.R
import app.simone.multiplayer.view.WaitingRoomActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by nicola on 01/08/2017.
 */
class FCMNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("FCM", "From: " + p0?.from)

        if(p0?.notification != null) {
            displayNotification(p0)
        }
    }

    private fun displayNotification(remoteMessage: RemoteMessage) {
        val notification = remoteMessage.notification
        val intent = Intent(this, WaitingRoomActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("match", remoteMessage.data["match"])
        intent.putExtra("recipient", remoteMessage.data["recipient"])
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.notification_icon_background))
                .setSmallIcon(R.drawable.notification_icon_background)
                .setContentTitle(notification.title)
                .setContentText(notification.body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0, notificationBuilder.build())
    }


}