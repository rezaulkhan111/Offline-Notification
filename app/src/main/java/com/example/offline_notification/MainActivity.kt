package com.example.offline_notification

import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    lateinit var btnClick: Button
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnClick = findViewById(R.id.btn_click)

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

        // Sets up permissions request launcher.
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1
                )
            }
        } else {
            Toast.makeText(this, "All permission accept", Toast.LENGTH_LONG).show()
        }

//        requestPermissionLauncher =
//            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
//                if (it) {
//                    timePickerDialog.show()
//                } else {
//                    Snackbar.make(
//                        findViewById<View>(android.R.id.content).rootView,
//                        "Please grant Notification permission from App Settings",
//                        Snackbar.LENGTH_LONG
//                    ).show()
//                }
//            }

        btnClick.setOnClickListener {
            timePickerDialog.show()
        }
    }
}
