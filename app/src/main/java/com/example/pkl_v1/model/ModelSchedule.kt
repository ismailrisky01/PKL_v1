package com.example.pkl_v1.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.example.pkl_v1.service.AktivitasService
import com.example.pkl_v1.service.AlarmBoardcasReceiver
import com.example.pkl_v1.service.AlarmReceiver
import com.example.pkl_v1.service.AlarmService
import java.util.*

class ModelSchedule() {
    var pendingIntent: PendingIntent? = null
    var pendingIntentAktivitas: PendingIntent? = null
    fun setAlarm(context: Context) {
        var time: Long
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 19
        calendar[Calendar.MINUTE] = 0
        val intent = Intent(context, AlarmBoardcasReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(context, 10, intent, 0)

        time = calendar.timeInMillis - calendar.timeInMillis % 60000
        if (System.currentTimeMillis() > time) {
            if (Calendar.AM_PM == 0) {
                time += 1000 * 60 * 60 * 12
            } else {
                time += 1000 * 60 * 60 * 24
            }
        }
//        System.currentTimeMillis() + 5000
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 5000, 86400000,
            pendingIntent
        )

    }

    fun cancleAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmBoardcasReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 10, intent, 0)
        alarmManager.cancel(pendingIntent)

    }

    fun setAktivitas(context: Context){
        val myIntent = Intent(context, AktivitasService::class.java)
        pendingIntentAktivitas = PendingIntent.getService(context, 1, myIntent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = 22
        calendar[Calendar.MINUTE] = 0
        assert(alarmManager != null)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis, AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
    fun cancleAktivitas(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AktivitasService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        alarmManager.cancel(pendingIntent)
    }
}