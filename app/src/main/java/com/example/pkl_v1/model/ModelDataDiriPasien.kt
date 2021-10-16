package com.example.pkl_v1.model

class ModelPasien(
    val idPasien: String,
    val namaPaien: String,
    val tanggalPasien: String,
    val fotoPasien: String
) {
    constructor() : this("", "", "", "")
}

