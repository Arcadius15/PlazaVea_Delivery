package edu.pe.idat.plazaveadelivery.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.plazaveadelivery.retrofit.PveaCliente
import edu.pe.idat.plazaveadelivery.retrofit.req.LoginReq
import edu.pe.idat.plazaveadelivery.retrofit.req.UsuarioPwsReq
import edu.pe.idat.plazaveadelivery.retrofit.res.LoginRes
import edu.pe.idat.plazaveadelivery.retrofit.res.MensajeRes
import edu.pe.idat.plazaveadelivery.retrofit.res.TiendaRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository {
    var loginRes = MutableLiveData<LoginRes?>()
    var tiendaRes = MutableLiveData<TiendaRes>()
    var mensajeRes = MutableLiveData<MensajeRes>()

    fun autenticarUser(request:LoginReq):MutableLiveData<LoginRes?>{
        val call:Call<LoginRes> = PveaCliente.retrofitService.login(request)
        call.enqueue(object :Callback<LoginRes>{
            override fun onResponse(call: Call<LoginRes>, response: Response<LoginRes>) {
                if (response.code() == 200){
                    loginRes.value = response.body()
                }else{
                    println("Error:"+response.code())
                    loginRes.value=null
                }
            }

            override fun onFailure(call: Call<LoginRes>, t: Throwable) {
                Log.e("Error en Login",t.message.toString())
            }
        })
        return loginRes
    }

    fun getTiendaDelUsuario(idTienda: String) : MutableLiveData<TiendaRes> {
        val call: Call<TiendaRes> = PveaCliente.retrofitService.getTiendaDelUsuario(idTienda)
        call.enqueue(object: Callback<TiendaRes>{
            override fun onResponse(call: Call<TiendaRes>, response: Response<TiendaRes>) {
                tiendaRes.value = response.body()
            }

            override fun onFailure(call: Call<TiendaRes>, t: Throwable) {
                Log.e("Error en Login",t.message.toString())
            }
        })

        return tiendaRes
    }

    fun editPassword(usuarioPswReq: UsuarioPwsReq) : MutableLiveData<MensajeRes>{
        val call: Call<MensajeRes> = PveaCliente
            .retrofitService.editPassword(usuarioPswReq)
        call.enqueue(object : Callback<MensajeRes>{
            override fun onResponse(call: Call<MensajeRes>, response: Response<MensajeRes>) {
                mensajeRes.value = response.body()
            }

            override fun onFailure(call: Call<MensajeRes>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }
        })

        return mensajeRes
    }
}