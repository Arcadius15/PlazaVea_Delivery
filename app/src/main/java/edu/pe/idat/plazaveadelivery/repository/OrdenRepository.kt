package edu.pe.idat.plazaveadelivery.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.plazaveadelivery.retrofit.PveaCliente
import edu.pe.idat.plazaveadelivery.retrofit.req.OrdenPatchReq
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenPageRes
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenRes
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenTotalElementsRes
import edu.pe.idat.plazaveadelivery.utils.ResponseHttp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdenRepository {

    var ordenId = MutableLiveData<String>()
    var ordenResponse = MutableLiveData<OrdenRes>()
    var responseHttp = MutableLiveData<ResponseHttp>()
    var ordenPageRes = MutableLiveData<OrdenPageRes>()
    var ordenTotalElementsRes = MutableLiveData<OrdenTotalElementsRes>()

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

    fun getOrdenesByTienda(idTienda: String, size: Int, token: String) : MutableLiveData<OrdenPageRes> {
        val call: Call<OrdenPageRes> = PveaCliente.ordenService.getOrdenesByTienda(idTienda, size, token)
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

    fun getListTotalElements(idTienda: String, token: String) : MutableLiveData<OrdenTotalElementsRes> {
        val call: Call<OrdenTotalElementsRes> = PveaCliente.ordenService.getListTotalElements(idTienda, token)
        call.enqueue(object : Callback<OrdenTotalElementsRes> {
            override fun onResponse(call: Call<OrdenTotalElementsRes>, response: Response<OrdenTotalElementsRes>) {
                ordenTotalElementsRes.value = response.body()
            }

            override fun onFailure(call: Call<OrdenTotalElementsRes>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }
        })

        return ordenTotalElementsRes
    }

    fun asignarseOrden(idOrden: String, idRepartidor: String, token: String) : MutableLiveData<ResponseHttp> {
        val call: Call<Void> = PveaCliente
            .ordenService.asignarseOrden(idOrden,idRepartidor,token)
        call.enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful){
                    responseHttp.value = ResponseHttp(
                        "Éxito",
                        response.isSuccessful,
                        "Correcto",
                        "No"
                    )
                } else {
                    responseHttp.value = ResponseHttp(
                        "Error",
                        response.isSuccessful,
                        "Problema",
                        "Sí"
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }
        })

        return responseHttp
    }

    fun actualizarOrden(idOrden: String, ordenPatchReq: OrdenPatchReq, token: String) : MutableLiveData<ResponseHttp> {
        val call: Call<Void> = PveaCliente
            .ordenService.actualizarOrden(idOrden,ordenPatchReq,token)
        call.enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful){
                    responseHttp.value = ResponseHttp(
                        "Éxito",
                        response.isSuccessful,
                        "Correcto",
                        "No"
                    )
                } else {
                    responseHttp.value = ResponseHttp(
                        "Error",
                        response.isSuccessful,
                        "Problema",
                        "Sí"
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }
        })

        return responseHttp
    }
}