package edu.pe.idat.plazaveadelivery.retrofit

import edu.pe.idat.plazaveadelivery.retrofit.req.LoginReq
import edu.pe.idat.plazaveadelivery.retrofit.req.ProductoReq
import edu.pe.idat.plazaveadelivery.retrofit.req.UsuarioPwsReq
import edu.pe.idat.plazaveadelivery.retrofit.res.LoginRes
import edu.pe.idat.plazaveadelivery.retrofit.res.MensajeRes
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenRes
import edu.pe.idat.plazaveadelivery.retrofit.res.TiendaRes
import retrofit2.Call
import retrofit2.http.*

interface AuthService {

    @POST("jwt/authenticate")
    fun login(@Body request:LoginReq):Call<LoginRes>

    @GET("tienda/{idTienda}")
    fun getTiendaDelUsuario(@Path("idTienda") idTienda: String): Call<TiendaRes>

    @PUT("jwt/editpassword")
    fun editPassword(@Body usuarioPswReq: UsuarioPwsReq) : Call<MensajeRes>
}