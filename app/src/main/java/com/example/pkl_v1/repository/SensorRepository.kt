package com.example.pkl_v1.repository

import androidx.lifecycle.LiveData
import com.example.pkl_v1.data.SensorDAO
import com.example.pkl_v1.data.SensorGyroDAO
import com.example.pkl_v1.model.ModelAcc
import com.example.pkl_v1.model.ModelGyro

class SensorRepository(private val sensorDAO: SensorDAO, private val sensorGyroDAO: SensorGyroDAO) {
    val readAllSensor: LiveData<List<ModelAcc>> = sensorDAO.readAllDataAcc()

    suspend fun addSensorAcc(modelAcc: ModelAcc) {
        sensorDAO.addAcc(modelAcc)
    }

    suspend fun avgYAcc(): Double {
        return sensorDAO.avgYAcc()
    }

    suspend fun avgXAcc(): Double {
        return sensorDAO.avgXAcc()
    }

    suspend fun avgZAcc(): Double {
        return sensorDAO.avgZAcc()
    }

    suspend fun deleteAllSensor() {
        sensorDAO.deleteAllAcc()
        sensorGyroDAO.deleteAllAcc()
    }

    //Gyro
    val readAllSensorGyro: LiveData<List<ModelGyro>> = sensorGyroDAO.readAllDataGyro()

    suspend fun addSensorGyro(modelGyro: ModelGyro) {
        sensorGyroDAO.addGyro(modelGyro)
    }
    suspend fun avgXGyro(): Double {
        return sensorGyroDAO.avgXGyro()
    }
    suspend fun avgYGyro(): Double {
        return sensorGyroDAO.avgYGyro()
    }
    suspend fun avgZGyro(): Double {
        return sensorGyroDAO.avgZGyro()
    }
}