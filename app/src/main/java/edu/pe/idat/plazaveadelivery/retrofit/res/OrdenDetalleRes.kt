package edu.pe.idat.plazaveadelivery.retrofit.res

import java.io.Serializable

data class OrdenDetalleRes(
    var cantidad: Int,
    var precio: Double,
    var producto: ProductoOrdenRes
) : Serializable
