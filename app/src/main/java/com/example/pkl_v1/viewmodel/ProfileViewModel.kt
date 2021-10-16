package com.example.pkl_v1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkl_v1.model.ModelDataDiriPasien
import com.example.pkl_v1.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository = Repository()
    fun setProfile(modelDataDiriPasien: ModelDataDiriPasien):Boolean {
        return viewModelScope.launch(Dispatchers.IO) {
            repository.setProfile(modelDataDiriPasien)
        }.isCompleted
    }
    fun getProfile(): LiveData<ModelDataDiriPasien> {
        return repository.getProfile()
    }
}