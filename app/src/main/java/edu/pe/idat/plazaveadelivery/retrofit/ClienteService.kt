package edu.pe.idat.plazaveadelivery.retrofit

import edu.pe.idat.plazaveadelivery.retrofit.res.ClienteRes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ClienteService {

    @GET("cliente/{idCliente}")
    fun findById(
        @Path("idCliente") idCliente: String, @Header("Authorization") token: String)
    : Call<ClienteRes>
}