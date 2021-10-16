package com.example.pkl_v1.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.IBinder
import android.os.Vibrator
import android.util.Log
import android.widget.DatePicker
import android.widget.RemoteViews
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDeepLinkBuilder
import com.example.pkl_v1.MainActivity
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
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

        var notificationManager: NotificationManager
        var notificationChannel: NotificationChannel
        var builder: Notification.Builder
        var contentView: RemoteViews
        val appID = "ID"
        val desc = "Desc"
        notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        contentView = RemoteViews(this?.packageName, R.layout.fragment_notifikasi)
        contentView.setTextViewText(R.id.ID_Notif_txtNotifTittle, "AntriKom")
        contentView.setTextViewText(R.id.ID_Notif_txtNotifDesc, "Kamu berhasil mengambil antrian!")
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntentq = PendingIntent.getActivity(
            this,
            12,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.questionnaireFragment)
            .createPendingIntent()


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(appID, desc, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GRAY
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, appID)
                .setContent(contentView)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources,
                        R.drawable.ic_launcher_foreground
                    )
                )
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
            notificationManager.notify(12, builder.build())
        }
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
