package edu.pe.idat.plazaveadelivery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.plazaveadelivery.repository.AuthRepository
import edu.pe.idat.plazaveadelivery.retrofit.req.LoginReq
import edu.pe.idat.plazaveadelivery.retrofit.res.LoginRes

class AuthViewModel:ViewModel() {
    var responseLogin:LiveData<LoginRes?>

    private var repository = AuthRepository()

    init {
        responseLogin = repository.loginRes
    }

    fun autenticarUsuario(usuario:String,password:String){
        responseLogin = repository.autenticarUser(
            LoginReq(usuario,password)
        )
    }

}