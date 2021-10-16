package com.example.pkl_v1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pkl_v1.model.ModelChat
import com.example.pkl_v1.model.ModelQuestion
import com.example.pkl_v1.repository.Repository

class NotifViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository = Repository()
    val readAllQuestion: LiveData<MutableList<ModelChat>> = repository.getMessage()

}