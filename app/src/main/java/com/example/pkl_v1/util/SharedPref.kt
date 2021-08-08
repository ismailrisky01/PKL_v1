package com.example.pkl_v1.util

import android.content.Context
import com.example.pkl_v1.model.AlarmModel

class SharedPref(context: Context) {
    var tinyDB = TinyDB(context)
    private val idAlarmKey = ""
    private val hourKey = ""
    private val minuteKey = ""
    private val startedKey = ""

    fun setData(alarmModel: AlarmModel){
        tinyDB.putInt(idAlarmKey,alarmModel.id) as Int
        tinyDB.putInt(hourKey,alarmModel.hour) as Int
        tinyDB.putInt(minuteKey,alarmModel.minute) as Int
        tinyDB.putBoolean(startedKey,alarmModel.started) as Boolean
    }

    fun getData():AlarmModel{
        val idAlarm = tinyDB.getInt(idAlarmKey)
        val hour = tinyDB.getInt(hourKey)
        val minute = tinyDB.getInt(minuteKey)
        val started = tinyDB.getBoolean(startedKey)
        return (AlarmModel(idAlarm,hour, minute,started))
    }

}
