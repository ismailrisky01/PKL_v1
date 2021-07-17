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
    val x_accelerometer: Int,
    val y_accelerometer: Int,
    val z_accelerometer: Int,
): Parcelable