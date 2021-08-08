package com.example.pkl_v1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkl_v1.model.ModelPasien
import com.example.pkl_v1.repository.DashboardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DashboardRepository = DashboardRepository()
    fun setProfile(modelPasien: ModelPasien):Boolean {
        return viewModelScope.launch(Dispatchers.IO) {
            repository.setProfile(modelPasien)
        }.isCompleted
    }
    fun getProfile(): LiveData<ModelPasien> {
        return repository.getProfile()
    }
}