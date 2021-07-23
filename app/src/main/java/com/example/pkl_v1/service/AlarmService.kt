package com.example.pkl_v1.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.os.Vibrator
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.example.pkl_v1.R
import com.example.pkl_v1.model.AlarmModel
import com.example.pkl_v1.viewmodel.AlarmViewModel
import java.text.SimpleDateFormat
import java.util.*

class AlarmService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
        mediaPlayer!!.isLooping = true
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        val notificationIntent = Intent(this, RingActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
//        val alarmTitle = String.format("%s Alarm", intent.getStringExtra(TITLE))
//        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle(alarmTitle)
//            .setContentText("Ring Ring .. Ring Ring")
//            .setSmallIcon(R.drawable.ic_alarm_black_24dp)
//            .setContentIntent(pendingIntent)
//            .build()
//        mediaPlayer!!.start()
//        val pattern = longArrayOf(0, 100, 1000)
//        vibrator!!.vibrate(pattern, 0)
//        startForeground(1, notification)
  return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.stop()
//        vibrator!!.cancel()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
class TimePickerMe(){

    var cal = Calendar.getInstance()
    val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
        cal.set(Calendar.MINUTE, minute)
    }
    fun getDatePicker(context: Context):AlarmModel{
        TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        val sdf =  SimpleDateFormat("HH:mm").format(cal.time)

        Toast.makeText(context, ""+sdf, Toast.LENGTH_SHORT).show()
        return AlarmModel(0,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true)


    }

}
fun logD(message: Any?) {
    Log.d("PKLKU", "Message : ${message ?: "Null"}")
}
