package edu.pe.idat.plazaveadelivery.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.plazaveadelivery.retrofit.PveaCliente
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenPageRes
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenRes
import edu.pe.idat.plazaveadelivery.utils.ResponseHttp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdenRepository {

    var ordenId = MutableLiveData<String>()
    var ordenResponse = MutableLiveData<OrdenRes>()
    var responseHttp = MutableLiveData<ResponseHttp>()
    var ordenPageRes = MutableLiveData<OrdenPageRes>()

    fun getOrden(idOrden: String, token: String) : MutableLiveData<OrdenRes> {
        val call: Call<OrdenRes> = PveaCliente.ordenService.getOrden(idOrden, token)
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

    fun getOrdenesByTienda(idTienda: String, page: Int, token: String) : MutableLiveData<OrdenPageRes> {
        val call: Call<OrdenPageRes> = PveaCliente.ordenService.getOrdenesByTienda(idTienda, page, token)
        call.enqueue(object : Callback<OrdenPageRes> {
            override fun onResponse(call: Call<OrdenPageRes>, response: Response<OrdenPageRes>) {
                ordenPageRes.value = response.body()
            }

            override fun onFailure(call: Call<OrdenPageRes>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }
        })

        return ordenPageRes
    }
}