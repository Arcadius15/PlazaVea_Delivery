package edu.pe.idat.plazaveadelivery.retrofit.req

data class UsuarioPwsReq(
    var email: String,
    var newPassword: String,
    var oldPassword: String
)
