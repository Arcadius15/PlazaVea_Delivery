package edu.pe.idat.plazaveadelivery.repository

import androidx.lifecycle.LiveData
import edu.pe.idat.plazaveadelivery.db.dao.TokenDAO
import edu.pe.idat.plazaveadelivery.db.entity.TokenEntity

class TokenRepository (private val tokenDAO: TokenDAO) {
    suspend fun insertar(tokenEntity: TokenEntity){
        tokenDAO.insertar(tokenEntity)
    }

    suspend fun actualizar(tokenEntity: TokenEntity){
        tokenDAO.actualizar(tokenEntity)
    }
    suspend fun eliminarToken(){
        tokenDAO.eliminarToken()
    }
    fun obtener(): LiveData<TokenEntity> {
        return tokenDAO.obtener()
    }
}