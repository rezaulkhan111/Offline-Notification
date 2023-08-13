package com.example.offline_notification

import android.app.Service
import android.content.Intent
import android.net.Uri
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
            NOTIFICATION_CHANNEL_DESC,
            NOTIFICATION_CHANNEL_ID
        )

        NotificationPlayer.playAudioRawFolder(
            this,
            Uri.parse("android.resource://" + packageName + "/" + R.raw.azan_common)
        )
        val notificationBuild = NotificationControl.createNotification(this).build()
        startForeground(NOTIFICATION_ID, notificationBuild)
        return START_STICKY
    }

    override fun onDestroy() {
        NotificationPlayer.releaseMediaPlayer()
        super.onDestroy()
    }
}