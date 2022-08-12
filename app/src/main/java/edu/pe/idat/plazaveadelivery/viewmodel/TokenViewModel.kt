package edu.pe.idat.plazaveadelivery.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import edu.pe.idat.plazaveadelivery.db.RoomDatabaseDelivery
import edu.pe.idat.plazaveadelivery.db.entity.TokenEntity
import edu.pe.idat.plazaveadelivery.repository.TokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TokenViewModel(application: Application)
    : AndroidViewModel(application){
        private val repo: TokenRepository

        init{
            val tokenDao = RoomDatabaseDelivery.getDatabase(application).tokenDAO()
            repo = TokenRepository(tokenDao)
        }

        fun instertar(tokenEntity : TokenEntity) = viewModelScope.launch(Dispatchers.IO){
            repo.insertar(tokenEntity)
        }

        fun actualizar(tokenEntity : TokenEntity) = viewModelScope.launch(Dispatchers.IO){
        repo.actualizar(tokenEntity)
        }

        fun eliminarToken() = viewModelScope.launch(Dispatchers.IO){
        repo.eliminarToken()
        }

        fun obtener(): LiveData<TokenEntity>{
            return repo.obtener()
        }
    }