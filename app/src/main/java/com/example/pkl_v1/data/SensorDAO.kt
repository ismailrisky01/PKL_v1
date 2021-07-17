package com.example.pkl_v1.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pkl_v1.model.ModelAcc

@Dao
interface SensorDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAcc(modelAcc: ModelAcc)

    @Update
    suspend fun updateAcc(modelAcc: ModelAcc)

    @Delete
    suspend fun deleteAcc(modelAcc: ModelAcc)

    @Query("DELETE FROM acc_table")
    suspend fun deleteAllAcc()

    @Query("SELECT * FROM acc_table ORDER BY id ASC")
    fun readAllDataAcc(): LiveData<List<ModelAcc>>
}