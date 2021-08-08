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
    val duduk: Int,
    val berdiri: Int,
    val berbaring: Int,
): Parcelable

@Parcelize
@Entity(tableName = "activity_nilai")
data class ModelNilai(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nlai: Double,

): Parcelable