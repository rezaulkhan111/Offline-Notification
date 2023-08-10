package com.example.offline_notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class NotificationDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "DISMISS_ACTION") {
            val notificationManager = NotificationManagerCompat.from(context!!)
            val notificationId = intent.getIntExtra("notification_id", 0)
            notificationManager.cancel(notificationId)
        }
    }
}