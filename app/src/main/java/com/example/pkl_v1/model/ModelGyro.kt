package com.example.pkl_v1.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "gyro_table")
data class ModelGyro (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val x_gyroscope: Double,
    val y_gyroscope: Double,
    val z_gyroscope: Double,
): Parcelable