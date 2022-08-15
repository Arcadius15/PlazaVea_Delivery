package edu.pe.idat.plazaveadelivery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.plazaveadelivery.repository.OrdenRepository
import edu.pe.idat.plazaveadelivery.retrofit.req.OrdenPatchReq
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenPageRes
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenRes
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenTotalElementsRes
import edu.pe.idat.plazaveadelivery.utils.ResponseHttp

class OrdenViewModel : ViewModel() {

    private var ordenId: LiveData<String>
    var responseHttp: LiveData<ResponseHttp>

    private var repository = OrdenRepository()

    init {
        ordenId = repository.ordenId
        responseHttp = repository.responseHttp
    }

    fun getOrden(idOrden: String, token: String): LiveData<OrdenRes>{
        return repository.getOrden(idOrden,token)
    }

    fun getOrdenesByTienda(idTienda: String, size: Int, token: String): LiveData<OrdenPageRes>{
        return repository.getOrdenesByTienda(idTienda, size, token)
    }

    fun getListTotalElements(idTienda: String, token: String): LiveData<OrdenTotalElementsRes>{
        return repository.getListTotalElements(idTienda, token)
    }

    fun asignarseOrden(idOrden: String, idRepartidor: String, token: String){
        responseHttp = repository.asignarseOrden(idOrden, idRepartidor, token)
    }

    fun actualizarOrden(idOrden: String, ordenPatchReq: OrdenPatchReq, token: String){
        responseHttp = repository.actualizarOrden(idOrden, ordenPatchReq, token)
    }
}