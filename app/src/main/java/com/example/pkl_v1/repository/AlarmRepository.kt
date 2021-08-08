package com.example.pkl_v1.repository

import androidx.lifecycle.LiveData
import com.example.pkl_v1.model.AlarmModel
import com.example.pkl_v1.data.alarm.AlarmDAO

class AlarmRepository(private val alarmDAO: AlarmDAO) {

    val readAllData: LiveData<List<AlarmModel>> = alarmDAO.readAllAlarm()

    suspend fun addAlarm(alarmModel: AlarmModel){
        alarmDAO.addAlarm(alarmModel)
    }
    suspend fun updateAlarm(alarmModel: AlarmModel){
        alarmDAO.update(alarmModel)
    }
    suspend fun deleteAllAlarm(){
        alarmDAO.deleteAllAlarm()
    }
    suspend fun deleteAlarm(alarmModel: AlarmModel){
        alarmDAO.deleteAlarm(alarmModel)
    }

}