package edu.pe.idat.plazaveadelivery.retrofit

import edu.pe.idat.plazaveadelivery.retrofit.req.OrdenAsignarRequest
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenPageRes
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenRes
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenTotalElementsRes
import retrofit2.Call
import retrofit2.http.*

interface OrdenService {

    @GET("orden/{idOrden}")
    fun getOrden(@Path("idOrden") idOrden: String,
                 @Header("Authorization") token: String): Call<OrdenRes>
    
    @GET("orden/tienda/{idTienda}")
    fun getOrdenesByTienda(@Path("idTienda") idTienda: String,
                           @Query("size") size: Int,
                           @Header("Authorization") token: String): Call<OrdenPageRes>

    @GET("orden/tienda/{idTienda}?size=1")
    fun getListTotalElements(@Path("idTienda") idTienda: String,
                          @Header("Authorization") token: String): Call<OrdenTotalElementsRes>

    @PUT("orden/repartidor/{idOrden}")
    fun asignarseOrden(@Path("idOrden") idOrden: String,
                        @Body ordenAsignarRequest: OrdenAsignarRequest,
                        @Header("Authorization") token: String): Call<Void>
}