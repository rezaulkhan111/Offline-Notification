package com.example.offline_notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import kotlin.math.abs

class NotificationBR : BroadcastReceiver() {
    private var alarmMgr: AlarmManager? = null
    private var pendingIntent: PendingIntent? = null
    private var FIVE_MINUTES: Long = 60000 * 5

    override fun onReceive(context: Context, intent: Intent) {
        val notifiTime = intent.getLongExtra(ACTION_PASS_Notification_TIME, -1)
        val timePassed = System.currentTimeMillis() >= notifiTime
        if (timePassed) {
            val renewTime = aaaaaaa()
            setNotification(context, renewTime, renewTime)

            Intent(context, NotificationService::class.java)
                .also {
                    startService(context, it)
                }
        }
    }

    private fun startService(context: Context, it: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(it)
            context.startService(it)
        } else {
            context.startService(it)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag", "ObsoleteSdkInt")
    fun setNotification(context: Context, notifiTime: Long, mRequestCode: Long) {
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationBR::class.java)
        intent.putExtra(ACTION_PASS_Notification_TIME, notifiTime)
        Log.e("NB", "setNotification: " + notifiTime)
        pendingIntent = PendingIntent.getBroadcast(
            context,
            mRequestCode.toInt(),
            intent,
            0
        )
        alarmMgr!!.setRepeating(
            AlarmManager.RTC_WAKEUP,
            notifiTime,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelNotification(context: Context, mRequestCode: Long) {
        context.let {
            if (alarmMgr == null) {
                alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            }
        }
        alarmMgr?.let {
            if (pendingIntent == null) {
                val intent = Intent(context, NotificationBR::class.java)
                pendingIntent = PendingIntent.getBroadcast(
                    context,
                    mRequestCode.toInt(),
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }
            it.cancel(pendingIntent)
        }
    }
}