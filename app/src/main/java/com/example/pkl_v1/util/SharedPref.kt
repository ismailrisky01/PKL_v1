package com.example.pkl_v1.util

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pkl_v1.model.AlarmModel

class SharedPref(context: Context) {
    var tinyDB = TinyDB(context)
    private val idAlarmKey = "idAlarmKey"
    private val hourKey = "hourKey"
    private val minuteKey = "minuteKey"
    private val startedKey = "startedKey"
    private val statusAlarmKey = "statusAlarmKey"

    private val sitKey = "sitKey"
    private val standKey = "standKey"
    private val walkKey = "walkKey"
    private val runKey = "runKey"

    fun setAlarmSetStatus(statusAlarm:Boolean){
        tinyDB.putBoolean(statusAlarmKey,statusAlarm)
    }
    fun getAlarmSetStatus():LiveData<Boolean>{
        val data = MutableLiveData<Boolean>()
        data.value = tinyDB.getBoolean(statusAlarmKey)
        return data
    }
    fun getAlarmStatus():Boolean{

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

    fun setSit(sit:Int){
        tinyDB.putInt(sitKey,sit)
    }
    fun getSit():Int{
        return tinyDB.getInt(sitKey)
    }

    fun setStand(sit:Int){
        tinyDB.putInt(standKey,sit)
    }
    fun getStand():Int{
        return tinyDB.getInt(standKey)
    }

    fun setWalk(sit:Int){
        tinyDB.putInt(walkKey,sit)
    }
    fun getWalk():Int{
        return tinyDB.getInt(walkKey)
    }

    fun setRun(sit:Int){
        tinyDB.putInt(runKey,sit)
    }
    fun getRun():Int{
        return tinyDB.getInt(runKey)
    }



}
