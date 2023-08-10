package com.example.offline_notification

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log

class NotificationService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("NS", "onStartCommand: ")
        NotificationControl.initNotificationManager(this)
        NotificationControl.createNotificationChannel(
            NOTIFICATION_CHANNEL_NAME,
            NOTIFICATION_CHANNEL_DESC, NOTIFICATION_CHANNEL_ID
        )
        val notificationBuilder = NotificationControl.createNotification(this)
        val notification = notificationBuilder.build()

        startForeground(122, notification)
        return START_STICKY
    }


    private fun notificationDeleteIntent(context: Context): PendingIntent {
        Log.e("", "getNotificationDeleteIntent: " + "delete")
        return PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, NotificationBR::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )
    }
}