package edu.pe.idat.plazaveadelivery.retrofit.res

data class ClienteRes(
    val apellidos: String,
    val direcciones: MutableList<DireccionRes>,
    val dni: String,
    val fechaNacimiento: String,
    val idCliente: String,
    val nombre: String,
    val numTelefonico: String
)
