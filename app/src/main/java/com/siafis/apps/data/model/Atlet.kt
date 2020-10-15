package com.siafis.apps.data.model

data class Atlet(
    var id: String? = "",
    var gender: String = "",
    val nama: String? = "",
    val umur: Int? = 0,
    val copyNilai:Double? = 0.0,
    val hasil: List<Hasil> = listOf()
)
