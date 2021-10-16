package com.example.pkl_v1.model

class ModelDataDiriPasien(
    val idPasien: String,
    val namaPaien: String,
    val tanggalPasien: String,
    val umurPasien:String,
    val domisili:String,
    val pekerjaan:String,
    val status:String,
    val jenisKelamin:String,
    val fotoPasien: String
) {
    constructor() : this("", "", "", "", "", "", "", "", "")
}

