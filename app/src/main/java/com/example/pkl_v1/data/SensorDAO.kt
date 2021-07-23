package com.example.pkl_v1.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pkl_v1.model.ModelAcc
import com.example.pkl_v1.model.ModelGyro

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

    @Query("SELECT avg(y_accelerometer) FROM acc_table ")
    suspend fun avgYAcc(): Double

    @Query("SELECT avg(x_accelerometer) FROM acc_table ")
    suspend fun avgXAcc(): Double

    @Query("SELECT avg(z_accelerometer) FROM acc_table ")
    suspend fun avgZAcc(): Double


}

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

    @Query("SELECT avg(x_gyroscope) FROM gyro_table ")
    suspend fun avgYGyro(): Double

    @Query("SELECT avg(y_gyroscope) FROM gyro_table ")
    suspend fun avgXGyro(): Double

    @Query("SELECT avg(z_gyroscope) FROM gyro_table ")
     fun avgZGyro(): Double


}