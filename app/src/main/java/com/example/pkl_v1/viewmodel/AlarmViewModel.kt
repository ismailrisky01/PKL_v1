package com.example.pkl_v1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkl_v1.data.DatabaseKu
import com.example.pkl_v1.model.AlarmModel
import com.example.pkl_v1.repository.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application):AndroidViewModel(application) {
    val readAllAlarm: LiveData<List<AlarmModel>>
    private val repository: AlarmRepository

    init {
        val alarmDao = DatabaseKu.getDatabase(application).alarmDao()
        repository = AlarmRepository(alarmDao)
        readAllAlarm = repository.readAllData
    }

    fun addAlarm(alarmModel: AlarmModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAlarm(alarmModel)
        }
    }
    fun deleteALLAlarm(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllAlarm()
        }
    }
    fun deleteAlarm(alarmModel: AlarmModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(alarmModel)
        }
    }
    fun upadteAlarm(alarmModel: AlarmModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAlarm(alarmModel)
        }
    }

}