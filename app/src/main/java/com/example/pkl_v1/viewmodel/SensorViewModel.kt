package com.example.pkl_v1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkl_v1.data.SensorDatabase
import com.example.pkl_v1.data.SensorDatabaseGyro
import com.example.pkl_v1.model.ModelAcc
import com.example.pkl_v1.model.ModelGyro
import com.example.pkl_v1.repository.SensorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SensorViewModel (application: Application): AndroidViewModel(application){
    val readAllSensorAcc:LiveData<List<ModelAcc>>
    val readAllSensorGyro:LiveData<List<ModelGyro>>
    private val repository: SensorRepository
    init {
        val sensorDao = SensorDatabase.getDatabase(application).sensorDAO()
        val sensorGyroDao = SensorDatabaseGyro.getDatabase(application).sensorGyroDAO()

        repository = SensorRepository(sensorDao,sensorGyroDao)
        readAllSensorAcc = repository.readAllSensor
        readAllSensorGyro = repository.readAllSensorGyro
    }
    fun addSensorAcc(modelAcc: ModelAcc){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSensorAcc(modelAcc)
        }
    }

    fun deleteALLSensor(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllSensor()

        }
    }
    suspend fun avgYAcc():Double{
            return repository.avgYAcc()
    }
    suspend fun avgXAcc():Double{
            return repository.avgXAcc()
    }
    suspend fun avgZAcc():Double{
            return repository.avgZAcc()
    }



    fun addSensorGyro(modelGyro: ModelGyro){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSensorGyro(modelGyro)
        }
    }

    suspend fun avgXGyro():Double{
        return repository.avgXGyro()
    }
    suspend fun avgYGyro():Double{
        return repository.avgYGyro()
    }
    suspend fun avgZGyro():Double{
        return repository.avgZGyro()
    }

}