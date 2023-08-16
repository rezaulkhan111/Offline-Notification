package com.example.offline_notification

import android.util.Log
import java.util.Calendar


const val ACTION_PASS_Notification_TIME = "NotificationTime"
const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION"
const val NOTIFICATION_CHANNEL_NAME = "Test Notification"
const val NOTIFICATION_CHANNEL_DESC = "TEST_NOTIFICATION_CHANNEL_DESC"
const val NOTIFICATION_ID = 122
const val DISMISS_CURRENT_NOTIFICATION_SERVICE = "dismissNotification"
const val ACTION_PASS_NOTIFICATION_SERVICE_TYPE = "alarmServiceType"
const val START_NOTIFICATION_SERVICE = "start"

fun currentHourMinuteMillis(): Long {
    val currentTimeMillis = System.currentTimeMillis()
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = currentTimeMillis

    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val minutes = calendar.get(Calendar.MINUTE)

    return (hours * 3600000 + minutes * 60000).toLong()
}

fun featureDateTime(): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()
    calendar.set(Calendar.HOUR_OF_DAY, 20) // 8 PM
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1)

    Log.e("CON", "featureDateTime: " + calendar.timeInMillis)

    return calendar.timeInMillis
}