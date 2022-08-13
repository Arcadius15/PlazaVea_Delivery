package edu.pe.idat.plazaveadelivery.retrofit.res

import java.io.Serializable

data class ProductoOrdenRes(
    var idProducto: String,
    var imagenUrl: String,
    var nombre: String
) : Serializable
