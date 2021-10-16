package com.example.pkl_v1.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "activity_table")
data class ModelActivity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val sit: Int,
    val stand: Int,
    val walk: Int,
    val run:Int
): Parcelable{
    constructor():this(0,0,0,0,0)
}


