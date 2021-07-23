package com.example.pkl_v1.repository

import androidx.lifecycle.LiveData
import com.example.pkl_v1.model.AlarmModel
import com.example.room_pkl.data.AlarmDAO

class AlarmRepository(private val alarmDAO: AlarmDAO) {

    val readAllData: LiveData<List<AlarmModel>> = alarmDAO.readAllAlarm()

    suspend fun addAlarm(alarmModel: AlarmModel){
        alarmDAO.addAlarm(alarmModel)
    }
    suspend fun updateAlarm(alarmModel: AlarmModel){
        alarmDAO.update(alarmModel)
    }
    suspend fun deleteAllAlarm(){
        alarmDAO.deleteAllUser()
    }

}