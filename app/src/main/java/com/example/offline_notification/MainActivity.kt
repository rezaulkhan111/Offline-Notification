package com.example.offline_notification

import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    lateinit var btnClick: Button
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnClick = findViewById(R.id.btn_click)

        // Sets up permissions request launcher.
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {

                } else {
                    Snackbar.make(
                        findViewById<View>(android.R.id.content).rootView,
                        "Please grant Notification permission from App Settings",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

        val notiRec = NotificationBR()
        val timePickerDialog: TimePickerDialog
        val calendar = Calendar.getInstance()
        calendar.apply {
            timePickerDialog = TimePickerDialog(
                this@MainActivity,
                { _: TimePicker, hourOfDay: Int, minute: Int ->
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, get(Calendar.HOUR_OF_DAY)) // 8 PM
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
//                    val selectedTime = formatTime(hourOfDay, minute)
//                notiRec.cancelNotification(this, selectedTime)
                    notiRec.setNotification(
                        this@MainActivity, timeInMillis, timeInMillis,
                        START_NOTIFICATION_SERVICE
                    )
                    // You can set the alarm using the selectedTime
                }, get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE), false
            )
        }
        btnClick.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                timePickerDialog.show()
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun formatTime(hour: Int, minute: Int): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        //        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
//        return sdf.format(cal.time)
        return (hour * 3600000 + minute * 60000).toLong()
    }
}
