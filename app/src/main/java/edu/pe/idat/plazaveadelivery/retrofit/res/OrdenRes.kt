package edu.pe.idat.plazaveadelivery.retrofit.res

import edu.pe.idat.plazaveadelivery.retrofit.req.ClienteIDReq
import edu.pe.idat.plazaveadelivery.retrofit.req.TiendaIDReq
import java.io.Serializable

data class OrdenRes (
    var direccion: String,
    var lat: Double,
    var lng: Double,
    var fecha: String,
    var fechaEntrega: String,
    var formaPago: String,
    var idOrden: String,
    var igv: Double,
    var monto: Double,
    var status: String,
    var tipoFop: Int,
    var total: Double,
    var cliente: ClienteIDReq,
    var tienda: TiendaIDReq,
    var ordendetalle: ArrayList<OrdenDetalleRes>,
    var repartidor: RepartidorOrdenRes?
) : Serializable