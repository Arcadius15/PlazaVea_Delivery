package edu.pe.idat.plazaveadelivery.retrofit

import edu.pe.idat.plazaveadelivery.utils.Constantes
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object PveaCliente {
    private var okHttpClient = OkHttpClient.Builder()
        .connectTimeout(1,TimeUnit.MINUTES)
        .readTimeout(30,TimeUnit.MINUTES)
        .writeTimeout(15,TimeUnit.MINUTES)
        .build()

    private fun buildRetrofit() = Retrofit.Builder().baseUrl(Constantes().API_PVEA_BASEURL).client(
        okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()

    val retrofitService:AuthService by lazy{
        buildRetrofit().create(AuthService::class.java)
    }
}