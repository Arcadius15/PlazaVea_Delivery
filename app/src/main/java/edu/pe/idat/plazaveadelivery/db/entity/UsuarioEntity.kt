package edu.pe.idat.plazaveadelivery.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class UsuarioEntity(
    @PrimaryKey
    var idUsuario: String
)
