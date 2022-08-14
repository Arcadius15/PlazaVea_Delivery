package edu.pe.idat.plazaveadelivery.retrofit

import edu.pe.idat.plazaveadelivery.retrofit.req.LoginReq
import edu.pe.idat.plazaveadelivery.retrofit.req.ProductoReq
import edu.pe.idat.plazaveadelivery.retrofit.res.LoginRes
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenRes
import retrofit2.Call
import retrofit2.http.*

interface AuthService {

    @POST("jwt/authenticate")
    fun login(@Body request:LoginReq):Call<LoginRes>

}