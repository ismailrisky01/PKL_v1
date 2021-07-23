package com.example.pkl_v1.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "acc_table")
data class ModelAcc (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val x_accelerometer: Double,
    val y_accelerometer: Double,
    val z_accelerometer: Double,
): Parcelable