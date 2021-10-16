package com.example.pkl_v1.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.NavDeepLinkBuilder
import com.example.pkl_v1.MainActivity
import com.example.pkl_v1.R
import com.example.pkl_v1.util.SharedPref
import java.util.*


class AlarmBoardcasReceiver : BroadcastReceiver() {
    private var mediaPlayer: MediaPlayer? = null

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    override fun onReceive(context: Context?, intent: Intent?) {
        SharedPref(context!!).setAlarmSetStatus(true)

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //set alarm manager dengan memasukkan waktu yang telah dikonversi menjadi milliseconds
        //set alarm manager dengan memasukkan waktu yang telah dikonversi menjadi milliseconds
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 19
        calendar[Calendar.MINUTE] = 0
//        if (Build.VERSION.SDK_INT >= 23) {
//            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
//
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
//        } else {
//            manager[AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()] = pendingIntent
//        }
        sendNotif(context)
    }

    fun sendNotif(context: Context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm)
        mediaPlayer?.start()
        var notificationManager: NotificationManager
        var notificationChannel: NotificationChannel
        var builder: Notification.Builder
        var contentView: RemoteViews
        val appID = "ID"
        val desc = "Desc"
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        contentView = RemoteViews(context.packageName, R.layout.fragment_notifikasi)
        contentView.setTextViewText(R.id.ID_Notif_txtNotifTittle, "F-Fit")
        contentView.setTextViewText(R.id.ID_Notif_txtNotifDesc, "harap isi Questionaire")
        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.questionnaireFragment)
            .createPendingIntent()
        val notificationIntent = Intent(context, MainActivity::class.java)
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
}