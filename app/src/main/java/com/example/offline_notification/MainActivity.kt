package com.example.offline_notification

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    lateinit var btnClick: Button
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
                    set(Calendar.HOUR_OF_DAY, hourOfDay) // 8 PM
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
//                    val selectedTime = formatTime(hourOfDay, minute)
//                notiRec.cancelNotification(this, selectedTime)
                    notiRec.setNotification(this@MainActivity, timeInMillis, timeInMillis)
                    // You can set the alarm using the selectedTime
                }, get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE), false
            )
        }
        btnClick.setOnClickListener {
            timePickerDialog.show()
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
