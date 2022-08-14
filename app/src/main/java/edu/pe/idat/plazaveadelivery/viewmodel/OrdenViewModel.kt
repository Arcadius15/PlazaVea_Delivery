package edu.pe.idat.plazaveadelivery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.plazaveadelivery.repository.OrdenRepository
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenPageRes
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenRes
import edu.pe.idat.plazaveadelivery.utils.ResponseHttp

class OrdenViewModel : ViewModel() {

    var ordenId: LiveData<String>
    var responseHttp: LiveData<ResponseHttp>

    private var repository = OrdenRepository()

    init {
        ordenId = repository.ordenId
        responseHttp = repository.responseHttp
    }

    fun getOrden(idOrden: String, token: String): LiveData<OrdenRes>{
        return repository.getOrden(idOrden,token)
    }

    fun getOrdenesByTienda(idTienda: String, page: Int, token: String): LiveData<OrdenPageRes>{
        return repository.getOrdenesByTienda(idTienda, page, token)
    }
}