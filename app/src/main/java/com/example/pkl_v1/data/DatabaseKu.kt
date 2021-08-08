package com.example.pkl_v1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pkl_v1.data.activity.ActivityDAO
import com.example.pkl_v1.data.activity.NilaiDAO
import com.example.pkl_v1.data.alarm.AlarmDAO
import com.example.pkl_v1.data.question.QuestionDAO
import com.example.pkl_v1.data.sensor.SensorGyroDAO
import com.example.pkl_v1.model.*

@Database(entities = arrayOf(AlarmModel::class,ModelActivity::class, ModelNilai::class,ModelQuestion::class,ModelGyro::class,ModelAcc::class),version = 1,exportSchema = false )
abstract class DatabaseKu: RoomDatabase() {
    abstract fun alarmDao(): AlarmDAO
    abstract fun activityDao(): ActivityDAO
    abstract fun nilaiDao(): NilaiDAO
    abstract fun questionDao(): QuestionDAO
    abstract fun sensorDAO():SensorDAO
    abstract fun sensorGyroDAO(): SensorGyroDAO



    companion object {
        @Volatile
        private var INSTANCE: DatabaseKu? = null

        fun getDatabase(context: Context): DatabaseKu {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseKu::class.java,
                    "database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}