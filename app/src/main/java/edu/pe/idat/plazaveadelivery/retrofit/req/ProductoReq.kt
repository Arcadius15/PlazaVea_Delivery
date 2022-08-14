package edu.pe.idat.plazaveadelivery.retrofit.req

import com.google.gson.Gson

data class ProductoReq(
    val idProducto: String,
    val imagenUrl: String,
    val nombre: String,
    val oferta: Boolean,
    val precioOferta: Double,
    val precioRegular: Double,
    var quantity: Int? = null
){
    fun toJson(): String{
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "ProductoReq(idProducto='$idProducto', imagenUrl='$imagenUrl', nombre='$nombre', oferta=$oferta, precioOferta=$precioOferta, precioRegular=$precioRegular, quantity=$quantity)"
    }


}
