package com.example.pkl_v1.data.question

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pkl_v1.model.AlarmModel
import com.example.pkl_v1.model.ModelQuestion

@Dao
interface QuestionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addQuestion(modelQuestion: ModelQuestion)

    @Update
    fun update(alarm: ModelQuestion?)

    @Query("SELECT * FROM question_table ORDER BY idSoal ASC")
    fun readAllQuestion(): LiveData<List<ModelQuestion>>

    @Query("SELECT * FROM question_table where dipilih==0 ORDER BY idSoal ASC")
    fun readNullQuestion(): List<ModelQuestion>

    @Query("DELETE FROM question_table")
    suspend fun deleteAllQuestion()

    @Query("SELECT sum(dipilih) FROM question_table ")
    suspend fun nilaiQUestioner(): Int
}