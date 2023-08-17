package com.example.offline_notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

object NotificationControl {
    private lateinit var notificationManager: NotificationManager

    fun initNotificationManager(context: Context) {
        if (!this::notificationManager.isInitialized) {
            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
    }

    fun createNotificationChannel(
        channelName: String,
        description: String,
        channelID: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                this.description = description
                setSound(
                    null,
                    null
                )
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
                setCustomContentView(generateCustomNotification(context, packageName))
                setContentText("Notification Message")
                setOngoing(false)
                priority = NotificationCompat.PRIORITY_DEFAULT
                setAutoCancel(true)

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

    @SuppressLint("RemoteViewLayout", "UnspecifiedImmutableFlag")
    fun generateCustomNotification(context: Context, packageName: String): RemoteViews {
        val notificationLayout = RemoteViews(packageName, R.layout.alarm_notification_view)

        notificationLayout.setTextViewText(
            R.id.tv_notification_title,
            context.resources.getString(R.string.app_name)
        )
        notificationLayout.setTextViewText(
            R.id.tv_notification_message,
            "message"
        )
        notificationLayout.setImageViewResource(
            R.id.iv_notification_dismiss,
            R.drawable.ic_baseline_cancel_24
        )

        val close = Intent(
            context,
            NotificationBR::class.java
        ).apply {
            putExtra(
                ACTION_PASS_NOTIFICATION_SERVICE_TYPE,
                DISMISS_CURRENT_NOTIFICATION_SERVICE
            )
        }
        notificationLayout.setOnClickPendingIntent(
            R.id.iv_notification_dismiss,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getBroadcast(
                    context,
                    0,
                    close,
                    PendingIntent.FLAG_MUTABLE or
                            PendingIntent.FLAG_UPDATE_CURRENT
                )
            } else {
                PendingIntent.getBroadcast(
                    context,
                    0,
                    close,
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
            }
        )
        return notificationLayout
    }

    private fun notificationDeleteIntent(context: Context): PendingIntent {
        Log.e("", "getNotificationDeleteIntent: " + "delete")
        return PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, NotificationBR::class.java)
                .putExtra(
                    ACTION_PASS_NOTIFICATION_SERVICE_TYPE,
                    DISMISS_CURRENT_NOTIFICATION_SERVICE
                ), PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )
    }
}