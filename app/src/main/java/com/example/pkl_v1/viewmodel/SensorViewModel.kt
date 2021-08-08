package com.example.pkl_v1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkl_v1.data.DatabaseKu
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
        val sensorDao = DatabaseKu.getDatabase(application).sensorDAO()
        val sensorGyroDao = DatabaseKu.getDatabase(application).sensorGyroDAO()

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


    suspend fun arrayXAcc():DoubleArray{
        return repository.arrayXAcc()
    }
    suspend fun arrayYAcc():DoubleArray{
        return repository.arrayYAcc()
    }
    suspend fun arrayZAcc():DoubleArray{
        return repository.arrayZAcc()
    }





    fun addSensorGyro(modelGyro: ModelGyro){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSensorGyro(modelGyro)
        }
    }
    suspend fun arrayXGyro():DoubleArray{
        return repository.arrayXGyro()
    }
    suspend fun arrayYGyro():DoubleArray{
        return repository.arrayYGyro()
    }
    suspend fun arrayZGyro():DoubleArray{
        return repository.arrayZGyro()
    }

}