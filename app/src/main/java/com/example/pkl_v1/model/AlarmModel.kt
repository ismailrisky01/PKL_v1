package com.example.pkl_v1.model

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Parcelable
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pkl_v1.R
import com.example.pkl_v1.databinding.FragmentAlarmBinding
import com.example.pkl_v1.service.logD
import com.example.room_pkl.data.AlarmBroadcastReceiver
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "alarm_table")
data class AlarmModel(
    @PrimaryKey(autoGenerate = true)
    val id :Int,
    val hour:Int,
    val minute:Int,
    var started:Boolean

    ):Parcelable{
        fun schedule(context: Context){

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar[Calendar.HOUR_OF_DAY] = hour
            calendar[Calendar.MINUTE] = minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
           setAlarm(calendar.timeInMillis,context)
            Toast.makeText(context, "Set + ${calendar.timeInMillis}", Toast.LENGTH_SHORT).show()
            Log.d("Alarm Bell", "Set + ${calendar.timeInMillis}")
        }
    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(context, id, intent, 0)
        alarmManager.cancel(alarmPendingIntent)
        this.started = false
        val toastText =
            String.format("Alarm cancelled for %02d:%02d with id %d", hour, minute, id)
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        Log.i("cancel", toastText)
    }
    }

fun setAlarm(timeInMillis:Long,context: Context){
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent()
    intent.setClass(context, MyAlarm::class.java)
    val alarmId = Random().nextInt(Int.MAX_VALUE)
    val pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)

}
 class MyAlarm : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        var mediaPlayer: MediaPlayer?=null
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm)
        mediaPlayer?.setOnPreparedListener {
            logD("Ready to GO")
        }
        mediaPlayer?.start()
        Log.d("Alarm Bell", "Alarm just fired")
      notif(context)

    }
}
fun notif(context: Context){

    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
    var notificationManager: NotificationManager
    var notificationChannel: NotificationChannel
    var builder: Notification.Builder
    var contentView: RemoteViews
    val appID = "ID"
    val desc = "Desc"
    notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    contentView = RemoteViews(context?.packageName, R.layout.fragment_notifikasi)
    contentView.setTextViewText(R.id.ID_Notif_txtNotifTittle, "AntriKom")
    contentView.setTextViewText(R.id.ID_Notif_txtNotifDesc, "Kamu berhasil mengambil antrian!")
    val notificationIntent = Intent(context, FragmentAlarmBinding::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context,
        12,
        notificationIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        notificationChannel =
            NotificationChannel(appID, desc, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GRAY
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)

        builder = Notification.Builder(context, appID)
            .setContent(contentView)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_launcher_foreground
                )
            )
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        notificationManager.notify(12, builder.build())
    }
}
