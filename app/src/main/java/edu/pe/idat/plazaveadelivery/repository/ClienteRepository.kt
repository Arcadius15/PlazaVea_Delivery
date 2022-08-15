package edu.pe.idat.plazaveadelivery.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.plazaveadelivery.retrofit.PveaCliente
import edu.pe.idat.plazaveadelivery.retrofit.res.ClienteRes
import edu.pe.idat.plazaveadelivery.utils.ResponseHttp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClienteRepository {

    var clienteResponse = MutableLiveData<ClienteRes>()
    var responseHttp = MutableLiveData<ResponseHttp>()

    fun findById(idCliente: String, token: String) : MutableLiveData<ClienteRes>{
        val call: Call<ClienteRes> = PveaCliente.clienteService.findById(idCliente, token)
        call.enqueue(object : Callback<ClienteRes>{
            override fun onResponse(call: Call<ClienteRes>, response: Response<ClienteRes>) {
                clienteResponse.value = response.body()
            }

            override fun onFailure(call: Call<ClienteRes>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }
        })

        return clienteResponse
    }
}