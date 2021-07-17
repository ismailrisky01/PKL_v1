package com.example.pkl_v1.repository

import androidx.lifecycle.LiveData
import com.example.pkl_v1.data.SensorDAO
import com.example.pkl_v1.model.ModelAcc

class SensorRepository(private val sensorDAO:SensorDAO) {
    val readAllSensor:LiveData<List<ModelAcc>> = sensorDAO.readAllDataAcc()

    suspend fun addSensor(modelAcc: ModelAcc){
        sensorDAO.addAcc(modelAcc)
    }
}