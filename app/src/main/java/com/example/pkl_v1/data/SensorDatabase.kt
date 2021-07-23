package com.example.pkl_v1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pkl_v1.model.ModelAcc
import com.example.pkl_v1.model.ModelGyro

@Database(entities =[ModelAcc::class],version = 1,exportSchema = false )
abstract class SensorDatabase:RoomDatabase() {
    abstract fun sensorDAO():SensorDAO

    companion object{
        @Volatile
        private var INSTANCE: SensorDatabase? = null

        fun getDatabase(context: Context): SensorDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SensorDatabase::class.java,
                    "sensor_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

@Database(entities =[ModelGyro::class],version = 1,exportSchema = false )
abstract class SensorDatabaseGyro:RoomDatabase() {
    abstract fun sensorGyroDAO():SensorGyroDAO

    companion object{
        @Volatile
        private var INSTANCE: SensorDatabaseGyro? = null

        fun getDatabase(context: Context): SensorDatabaseGyro{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SensorDatabaseGyro::class.java,
                    "sensor_gyro_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}