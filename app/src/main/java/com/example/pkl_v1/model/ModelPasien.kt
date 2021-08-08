package com.example.pkl_v1.model

class ModelPasien(
    val idPasien: String,
    val namaPaien: String,
    val umurPasien: String,
    val jenisKelamin: String,
    val tinggiPasien: String,
    val beratPasien: String
) {
    constructor() : this("", "", "", "", "", "")
}

class ModelAktifitasPasein(
    val idPasien: String,
    val dudukPasien: Double,
    val berdiriPasien: Double,
    val tidurPasien: Double
) {
    constructor() : this("", 0.0, 0.0, 0.0)
}
