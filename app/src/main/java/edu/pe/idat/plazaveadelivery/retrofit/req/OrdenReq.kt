package edu.pe.idat.plazaveadelivery.retrofit.req

data class OrdenReq(
    var cliente: ClienteIDReq,
    var direccion: String,
    var fecha: String,
    var formaPago: String,
    var igv: Double,
    var monto: Double,
    var ordendetalle: ArrayList<OrdendetalleReq>,
    var status: String,
    var tienda: TiendaIDReq,
    var tipoFop: Int,
    var total: Double
)
