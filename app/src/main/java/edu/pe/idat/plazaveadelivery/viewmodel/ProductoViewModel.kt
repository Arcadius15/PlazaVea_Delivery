package edu.pe.idat.plazaveadelivery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.plazaveadelivery.repository.AuthRepository
import edu.pe.idat.plazaveadelivery.repository.ProductosRepository
import edu.pe.idat.plazaveadelivery.retrofit.req.ProductoReq

class ProductoViewModel: ViewModel() {

    var responseProducto:LiveData<ProductoReq?>

    private var repository = ProductosRepository()

    init {
        responseProducto = repository.producto
    }

    fun findById(idProducto: String): LiveData<ProductoReq> {
        return repository.findById(idProducto)
    }


}