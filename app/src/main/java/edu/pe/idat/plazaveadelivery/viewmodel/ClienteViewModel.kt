package edu.pe.idat.plazaveadelivery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.plazaveadelivery.repository.ClienteRepository
import edu.pe.idat.plazaveadelivery.retrofit.res.ClienteRes
import edu.pe.idat.plazaveadelivery.utils.ResponseHttp

class ClienteViewModel: ViewModel() {

    var clienteResponse: LiveData<ClienteRes>

    private var repository = ClienteRepository()

    init{
        clienteResponse = repository.clienteResponse
    }

    fun findById(idCliente:String, token: String): LiveData<ClienteRes>{
        return repository.findById(idCliente, token)
    }
}