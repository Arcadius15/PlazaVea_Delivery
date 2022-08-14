package edu.pe.idat.plazaveadelivery.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class UsuarioEntity(
    @PrimaryKey
    var idRepartidor: String,
    var email: String,
    var nombre: String,
    var apellidos: String,
    var dni: String,
    var direccion: String,
    var numTelefonico: String,
    var placa: String,
    var idTienda: String
)
