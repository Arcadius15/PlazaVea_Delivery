package edu.pe.idat.plazaveadelivery.retrofit

import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenPageRes
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenRes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface OrdenService {

    @GET("orden/{idOrden}")
    fun getOrden(@Path("idOrden") idOrden: String,
                 @Header("Authorization") token: String): Call<OrdenRes>
    
    @GET("orden/tienda/{idTienda}")
    fun getOrdenesByTienda(@Path("idTienda") idTienda: String,
                           @Query("page") page: Int,
                           @Header("Authorization") token: String): Call<OrdenPageRes>

}