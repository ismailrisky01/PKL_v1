package com.example.pkl_v1.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pkl_v1.model.ModelDataDiriPasien
import com.example.pkl_v1.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AuthRepository = AuthRepository()

    fun setDataRegis(modelDataDiriPasien: ModelDataDiriPasien, context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            repository.setData(modelDataDiriPasien,context)
        }
    }
    fun login(email:String,password:String,context: Context,task:Unit){
        repository.loginUser(email,password,context,task)
    }
}