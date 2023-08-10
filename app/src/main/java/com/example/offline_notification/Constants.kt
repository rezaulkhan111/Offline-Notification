package com.example.offline_notification

import android.app.AlarmManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log
import java.util.Calendar
import java.util.concurrent.TimeUnit


const val ACTION_PASS_Notification_TIME = "NotificationTime"
const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION"
const val NOTIFICATION_CHANNEL_NAME = "Test Notification"
const val NOTIFICATION_CHANNEL_DESC = "TEST_NOTIFICATION_CHANNEL_DESC"

fun currentHourMinuteMillis(): Long {
    val currentTimeMillis = System.currentTimeMillis()
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = currentTimeMillis

    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val minutes = calendar.get(Calendar.MINUTE)

    return (hours * 3600000 + minutes * 60000).toLong()
}

fun aaaaaaa(): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()
    calendar.set(Calendar.HOUR_OF_DAY, 20) // 8 PM
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1)

    Log.e("CON", "aaaaaaa: " + calendar.timeInMillis)

    return calendar.timeInMillis
}