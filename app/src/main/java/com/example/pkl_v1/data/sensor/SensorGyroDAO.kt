package com.example.pkl_v1.data.sensor

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pkl_v1.model.ModelGyro

@Dao
interface SensorGyroDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGyro(modelGyro: ModelGyro)

    @Update
    suspend fun updateAcc(modelGyro: ModelGyro)

    @Delete
    suspend fun deleteAcc(modelGyro: ModelGyro)

    @Query("DELETE FROM gyro_table")
    suspend fun deleteAllAcc()

    @Query("SELECT * FROM gyro_table ORDER BY id ASC")
    fun readAllDataGyro(): LiveData<List<ModelGyro>>

    @Query("SELECT x_gyroscope FROM gyro_table ")
    suspend fun arrayXGyro(): DoubleArray

    @Query("SELECT y_gyroscope FROM gyro_table ")
    suspend fun arrayYGyro(): DoubleArray

    @Query("SELECT z_gyroscope FROM gyro_table ")
    suspend fun arrayZGyro(): DoubleArray

}