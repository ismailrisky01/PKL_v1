package com.example.pkl_v1.viewmodel

import android.app.Activity
import android.app.Application
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkl_v1.data.DatabaseKu
import com.example.pkl_v1.model.ModelQuestion
import com.example.pkl_v1.repository.QuestionRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuestionViewModel(application: Application) : AndroidViewModel(application) {
    val readAllQuestion: LiveData<List<ModelQuestion>>
    private val repository: QuestionRepository

    init {
        val alarmDao = DatabaseKu.getDatabase(application).questionDao()
        repository = QuestionRepository(alarmDao)
        readAllQuestion = repository.readAllData
    }

    fun addData(modelQuestion: ModelQuestion) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addQuestion(modelQuestion)
        }
    }

    fun deleteALLData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllQuestion()
        }
    }

    fun upadteQuestion(modelQuestion: ModelQuestion) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateQuestion(modelQuestion)
        }
    }
    fun downloadData():LiveData<MutableList<ModelQuestion>>{
        return repository.downloadData()
    }

    suspend fun nilai(): Int {
        return repository.nilaiQuestion()
    }

    fun uploadNilai(activity: FragmentActivity,view: View) {
        repository.saveNilai(activity,view)
    }

}