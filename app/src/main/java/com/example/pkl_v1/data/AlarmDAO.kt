package com.example.room_pkl.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pkl_v1.model.AlarmModel

@Dao
interface AlarmDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlarm(alarmModel: AlarmModel)

    @Update
    fun update(alarm: AlarmModel?)

    @Query("SELECT * FROM alarm_table ORDER BY id ASC")
    fun readAllAlarm(): LiveData<List<AlarmModel>>
    @Query("DELETE FROM alarm_table")
    suspend fun deleteAllUser()
}