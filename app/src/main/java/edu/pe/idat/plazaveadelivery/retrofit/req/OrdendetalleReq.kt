package edu.pe.idat.plazaveadelivery.retrofit.req

data class OrdendetalleReq(
    var cantidad: Int,
    var precio: Double,
    var producto: ProductoIDReq
)
