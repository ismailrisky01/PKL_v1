package com.example.pkl_v1.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkl_v1.model.ModelActivity
import com.example.pkl_v1.model.ModelDataDiriPasien
import com.example.pkl_v1.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository = Repository()
    fun setProfile(modelDataDiriPasien: ModelDataDiriPasien): Boolean {
        return viewModelScope.launch(Dispatchers.IO) {
            repository.setProfile(modelDataDiriPasien)
        }.isCompleted
    }

    fun getProfile(): LiveData<ModelDataDiriPasien> {
        return repository.getProfile()
    }


    fun uploadSensor(context:Context,modelActivity:ModelActivity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.setActivityPasien(context,modelActivity)
        }
    }
    fun getSensor(date:String):LiveData<ModelActivity>{
        return repository.getActivityPasien(date)
    }
}