package com.example.offline_notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationControl {

    private lateinit var notificationManager: NotificationManager

    fun initNotificationManager(context: Context) {
        if (!this::notificationManager.isInitialized) {
            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
    }

    fun createNotificationChannel(channelName: String, description: String, channelID: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelID,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    this.description = description
                    setSound(null, null)
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                })
        }
    }

    fun createNotification(
        context: Context,
    ): NotificationCompat.Builder {
        val pmAppManager: PackageManager = context.packageManager
        val packageName = context.packageName
        val intent: Intent = pmAppManager.getLaunchIntentForPackage(packageName)!!

        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .apply {
                setSmallIcon(R.drawable.ic_launcher_foreground)
                setContentTitle(context.resources.getString(R.string.app_name))
                setContentText("test")
                setAutoCancel(true)
//                setDeleteIntent(notificationDeleteIntent(context))

                priority = NotificationCompat.PRIORITY_DEFAULT

                setContentIntent(
                    PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                )
            }
    }
}