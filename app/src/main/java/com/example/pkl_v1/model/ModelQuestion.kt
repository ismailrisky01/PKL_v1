package com.example.pkl_v1.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "question_table")
class ModelQuestion(
    @PrimaryKey
    val idSoal: String,
    val noSoal:Int,
    val soal: String,
    val pilihan1: String,
    val pilihan2: String,
    val pilihan3: String,
    val pilihan4: String,
    val pilihan5: String,
    val dipilih:Int
) : Parcelable{
    constructor():this("",0,"","","","","","",0)
}