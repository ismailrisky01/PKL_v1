package com.example.pkl_v1.repository

import androidx.lifecycle.LiveData
import com.example.pkl_v1.data.SensorDAO
import com.example.pkl_v1.data.sensor.SensorGyroDAO
import com.example.pkl_v1.model.ModelAcc
import com.example.pkl_v1.model.ModelGyro

class SensorRepository(private val sensorDAO: SensorDAO, private val sensorGyroDAO: SensorGyroDAO) {
    val readAllSensor: LiveData<List<ModelAcc>> = sensorDAO.readAllDataAcc()

    suspend fun addSensorAcc(modelAcc: ModelAcc) {
        sensorDAO.addAcc(modelAcc)
    }


    suspend fun arrayXAcc(): DoubleArray {
        return sensorDAO.arrayXAcc()
    }
   suspend fun arrayYAcc(): DoubleArray {
        return sensorDAO.arrayYAcc()
    }
    suspend fun arrayZAcc(): DoubleArray {
        return sensorDAO.arrayZAcc()
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
    suspend fun arrayXGyro(): DoubleArray {
        return sensorGyroDAO.arrayXGyro()
    }
    suspend fun arrayYGyro(): DoubleArray {
        return sensorGyroDAO.arrayYGyro()
    }
    suspend fun arrayZGyro(): DoubleArray {
        return sensorGyroDAO.arrayZGyro()
    }
}