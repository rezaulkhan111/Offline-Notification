package com.example.offline_notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class NotificationBR : BroadcastReceiver() {
    private var alarmMgr: AlarmManager? = null
    private var pendingIntent: PendingIntent? = null

    override fun onReceive(context: Context, intent: Intent) {
        val receivedIntentValue = intent.getStringExtra(ACTION_PASS_NOTIFICATION_SERVICE_TYPE)
        Log.e("NBR", "onReceive: $receivedIntentValue")
        val notifiTime = intent.getLongExtra(ACTION_PASS_Notification_TIME, -1)
        val timePassed = System.currentTimeMillis() >= notifiTime

        if (timePassed) {
            when (receivedIntentValue) {
                START_NOTIFICATION_SERVICE -> {
                    Intent(context, NotificationService::class.java)
                        .also {
                            startService(context, it)
                        }

                    val renewTime = featureDateTime()
                    setNotification(
                        context,
                        renewTime,
                        renewTime,
                        START_NOTIFICATION_SERVICE
                    )
                }

                DISMISS_CURRENT_NOTIFICATION_SERVICE -> {
                    NotificationPlayer.releaseMediaPlayer()
                    Intent(context, NotificationService::class.java)
                        .also {
                            context.stopService(it)
                        }
                }
            }
        }
    }

    private fun startService(context: Context, it: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.startForegroundService(it)
            context.startService(it)
        } else {
            context.startService(it)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag", "ObsoleteSdkInt")
    fun setNotification(
        context: Context,
        notifiTime: Long,
        mRequestCode: Long,
        requestType: String
    ) {
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationBR::class.java)
        intent.putExtra(ACTION_PASS_NOTIFICATION_SERVICE_TYPE, requestType)
        intent.putExtra(ACTION_PASS_Notification_TIME, notifiTime)
        Log.e("NB", "setNotification: " + notifiTime)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.e("NBR", "setNotification: if")
            pendingIntent = PendingIntent.getActivity(
                context,
                mRequestCode.toInt(),
                intent,
                PendingIntent.FLAG_MUTABLE or
                        PendingIntent.FLAG_UPDATE_CURRENT
            )

            alarmMgr!!.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                notifiTime,
                pendingIntent
            )
        } else {
            Log.e("NBR", "setNotification: else")
            pendingIntent = PendingIntent.getBroadcast(
                context,
                mRequestCode.toInt(),
                intent,
                PendingIntent.FLAG_MUTABLE
            )
            alarmMgr!!.setRepeating(
                AlarmManager.RTC_WAKEUP,
                notifiTime,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    fun cancelNotification(context: Context, mRequestCode: Long, requestType: String) {
        context.let {
            if (alarmMgr == null) {
                alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            }
        }
        alarmMgr?.let {
            if (pendingIntent == null) {
                val intent = Intent(context, NotificationBR::class.java)
                intent.putExtra(ACTION_PASS_NOTIFICATION_SERVICE_TYPE, requestType)
                pendingIntent = PendingIntent.getBroadcast(
                    context,
                    mRequestCode.toInt(),
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT or
                            PendingIntent.FLAG_IMMUTABLE
                )
            }
            it.cancel(pendingIntent)
        }
    }
}