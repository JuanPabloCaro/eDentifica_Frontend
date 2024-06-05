package com.app.edentifica.utils.firebaseMessaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.edentifica.MainActivity
import com.app.edentifica.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

class MyFirebaseService : FirebaseMessagingService() {


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        showNotification(message)
    }

    private fun showNotification(message: RemoteMessage) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val notification = NotificationCompat.Builder(this, MyApp.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setSmallIcon(R.drawable.ic_google)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(1, notification)
    }




//    private val notificationIdCounter = AtomicInteger(0)
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        remoteMessage.notification?.let { notification ->
//            Log.i("FCM Title", "${notification.title}")
//            Log.i("FCM Body", "${notification.body}")
//            sendNotification(notification)
//        }
//    }
//
//    private fun sendNotification(notification: RemoteMessage.Notification) {
//        val intent = Intent(this, MainActivity::class.java).apply {
//            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        }
//
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val channelId = getString(R.string.default_notification_channel_id)
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setContentTitle(notification.title)
//            .setContentText(notification.body)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setSmallIcon(R.drawable.ic_google)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
//
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        val notificationId = notificationIdCounter.incrementAndGet()
//        notificationManager.notify(notificationId, notificationBuilder.build())
//    }
//
//    override fun onNewToken(token: String) {
//        Log.d("FCM", "New token: $token")
//        // Implementa aquí el envío del token al servidor
//    }
//
//    companion object {
//        const val CHANNEL_NAME = "FCM Notification Channel"
//    }
}
