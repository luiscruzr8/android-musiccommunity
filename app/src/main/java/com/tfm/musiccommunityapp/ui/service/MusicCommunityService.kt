package com.tfm.musiccommunityapp.ui.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.ui.base.MainActivity

class MusicCommunityService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d("MusicCommunityService", "Message data payload: " + remoteMessage.data)
            sendNotification(remoteMessage)
            // Handle the data message here.
            // For example, you could create a notification with this data.
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MusicCommunityService", "Refreshed token: $token")
        // Here, you would send the token to your server during signup/signin flows
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(notification: RemoteMessage) {
        val notificationId = System.currentTimeMillis().toInt()  // Creating a unique notification ID based on the current time

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, "com.tfm.musiccommunityapp.urgent")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(notification.data["title"] ?: "Default title")
            .setContentText(notification.data["body"] ?: "Default content")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, notificationBuilder.build())
        }
    }


}