package edu.pe.idat.plazaveadelivery.retrofit.res

data class UsuarioJWTResponse(
    var apellidos: String,
    var direccion: String,
    var dni: String,
    var fechaNacimiento: String,
    var idRepartidor: String,
    var nombre: String,
    var numTelefonico: String,
    var placa: String,
    var status: String,
    var turno: Int,
    var idTienda: String
)