package edu.pe.idat.plazaveadelivery.retrofit.res

import java.io.Serializable

data class DireccionRes(
    val idDireccion : Int,
    val direccion: String,
    val latitud: Double,
    val longitud: Double
) : Serializable
