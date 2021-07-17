package com.example.pkl_v1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkl_v1.data.SensorDatabase
import com.example.pkl_v1.model.ModelAcc
import com.example.pkl_v1.repository.SensorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SensorViewModel (application: Application): AndroidViewModel(application){
    val readAllSensor:LiveData<List<ModelAcc>>
    private val repository: SensorRepository
    init {
        val sensorDao = SensorDatabase.getDatabase(application).sensorDAO()
        repository = SensorRepository(sensorDao)
        readAllSensor = repository.readAllSensor
    }
    fun addSensor(modelAcc: ModelAcc){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSensor(modelAcc)
        }
    }
}