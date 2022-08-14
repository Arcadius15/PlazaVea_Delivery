package edu.pe.idat.plazaveadelivery.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.plazaveadelivery.retrofit.PveaCliente
import edu.pe.idat.plazaveadelivery.retrofit.req.ProductoReq
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductosRepository {

    var producto = MutableLiveData<ProductoReq>()

    fun findById(idProducto: String): LiveData<ProductoReq> {
        val call: Call<ProductoReq> = PveaCliente.retrofitService.findById(idProducto)
        call.enqueue(object : Callback<ProductoReq> {
            override fun onResponse(call: Call<ProductoReq>, response: Response<ProductoReq>) {
                producto.value = response.body()
            }

            override fun onFailure(call: Call<ProductoReq>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }

        })

        return producto
    }
}