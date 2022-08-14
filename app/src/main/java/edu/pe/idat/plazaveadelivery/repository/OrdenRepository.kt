package edu.pe.idat.plazaveadelivery.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.plazaveadelivery.retrofit.PveaCliente
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenRes
import edu.pe.idat.plazaveadelivery.utils.ResponseHttp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdenRepository {

    var ordenId = MutableLiveData<String>()
    var ordenResponse = MutableLiveData<OrdenRes>()
    var responseHttp = MutableLiveData<ResponseHttp>()

    fun getOrden(idOrden: String, token: String) : MutableLiveData<OrdenRes> {
        val call: Call<OrdenRes> = PveaCliente.retrofitService.getOrden(idOrden, token)
        call.enqueue(object : Callback<OrdenRes> {
            override fun onResponse(call: Call<OrdenRes>, response: Response<OrdenRes>) {
                ordenResponse.value = response.body()
            }

            override fun onFailure(call: Call<OrdenRes>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }
        })

        return ordenResponse
    }
}