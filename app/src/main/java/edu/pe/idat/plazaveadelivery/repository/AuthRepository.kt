package edu.pe.idat.plazaveadelivery.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.plazaveadelivery.retrofit.PveaCliente
import edu.pe.idat.plazaveadelivery.retrofit.req.LoginReq
import edu.pe.idat.plazaveadelivery.retrofit.res.LoginRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository {
    var loginRes = MutableLiveData<LoginRes?>()

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
}