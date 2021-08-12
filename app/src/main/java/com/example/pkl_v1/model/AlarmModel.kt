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
import androidx.navigation.NavDeepLinkBuilder
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pkl_v1.MainActivity
import com.example.pkl_v1.R
import com.example.pkl_v1.service.logD
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "alarm_table")
data class AlarmModel(
    @PrimaryKey
    val id: Int,
    val hour: Int,
    val minute: Int,
    var started: Boolean

) : Parcelable {
    fun schedule(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MyAlarm::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_NO_CREATE)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        val RUN_DAILY = (24 * 60 * 60 * 1000).toLong()
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            RUN_DAILY,
            pendingIntent
        )
        Toast.makeText(context, "Succes Set Alarm with id$id", Toast.LENGTH_SHORT).show()
        val toastText = String.format("Alarm started for %02d:%02d with id %d", hour, minute, id)
        Log.d("PKL_ismail", toastText)
        this.started = true
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MyAlarm::class.java)
        val alarmPendingIntent =
            PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(alarmPendingIntent)
        this.started = false

        val toastText =
            String.format("Alarm cancelled for %02d:%02d with id %d", hour, minute, id)
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        Log.i("PKL_ismail", toastText)
    }
}

class MyAlarm : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        var mediaPlayer: MediaPlayer? = null
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm)
        mediaPlayer?.setOnPreparedListener {
            logD("Ready to GO")
        }
        mediaPlayer?.start()
        Log.d("Alarm Bell", "Alarm just fired")
        notif(context)
    }
}


fun notif(context: Context) {

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
    val notificationIntent = Intent(context, MainActivity::class.java)
    val pendingIntentq = PendingIntent.getActivity(
        context,
        12,
        notificationIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val pendingIntent = NavDeepLinkBuilder(context)
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
