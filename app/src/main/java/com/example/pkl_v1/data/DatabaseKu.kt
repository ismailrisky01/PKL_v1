package com.example.pkl_v1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pkl_v1.model.*

@Database(entities = arrayOf(AlarmModel::class, ModelQuestion::class),version = 1,exportSchema = false )
abstract class DatabaseKu: RoomDatabase() {
    abstract fun alarmDao(): AlarmDAO
    abstract fun questionDao(): QuestionDAO



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