package edu.pe.idat.plazaveadelivery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.plazaveadelivery.repository.AuthRepository
import edu.pe.idat.plazaveadelivery.retrofit.req.LoginReq
import edu.pe.idat.plazaveadelivery.retrofit.req.UsuarioPwsReq
import edu.pe.idat.plazaveadelivery.retrofit.res.LoginRes
import edu.pe.idat.plazaveadelivery.retrofit.res.MensajeRes
import edu.pe.idat.plazaveadelivery.retrofit.res.TiendaRes

class AuthViewModel:ViewModel() {
    var responseLogin:LiveData<LoginRes?>
    var mensajeRes: LiveData<MensajeRes>

    private var repository = AuthRepository()

    init {
        responseLogin = repository.loginRes
        mensajeRes = repository.mensajeRes
    }

    fun autenticarUsuario(usuario:String,password:String){
        responseLogin = repository.autenticarUser(
            LoginReq(usuario,password)
        )
    }

    fun getTiendaDelUsuario(idTienda: String): LiveData<TiendaRes> {
        return repository.getTiendaDelUsuario(idTienda)
    }

    fun editPassword(usuarioPswRequest: UsuarioPwsReq){
        mensajeRes = repository.editPassword(usuarioPswRequest)
    }
}