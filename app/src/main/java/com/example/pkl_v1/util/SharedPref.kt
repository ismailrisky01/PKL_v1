package com.example.pkl_v1.util

import android.content.Context
import com.example.pkl_v1.model.AlarmModel

class SharedPref(context: Context) {
    var tinyDB = TinyDB(context)
    private val idAlarmKey = "idAlarmKey"
    private val hourKey = "hourKey"
    private val minuteKey = "minuteKey"
    private val startedKey = "startedKey"
    private val statusAlarmKey = "statusAlarmKey"

    fun setAlarmSetStatus(statusAlarm:Boolean){
        tinyDB.putBoolean(statusAlarmKey,statusAlarm)
    }
    fun getAlarmSetStatus():Boolean{
        return tinyDB.getBoolean(statusAlarmKey)
    }

    fun setData(alarmModel: AlarmModel){
        tinyDB.putInt(idAlarmKey,alarmModel.id)
        tinyDB.putInt(hourKey,alarmModel.hour)
        tinyDB.putInt(minuteKey,alarmModel.minute)
        tinyDB.putBoolean(startedKey,alarmModel.started)
    }

    fun getData():AlarmModel{
        val idAlarm = tinyDB.getInt(idAlarmKey)
        val hour = tinyDB.getInt(hourKey)
        val minute = tinyDB.getInt(minuteKey)
        val started = tinyDB.getBoolean(startedKey)
        return (AlarmModel(idAlarm,hour, minute,started))
    }

}
