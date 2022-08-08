package edu.pe.idat.plazaveadelivery.utils

import android.app.Application
import android.content.Context

class MiApp :Application(){
    init { INSTANCE = this }

    //Agrupa todos las variables y métodos que están definidos como
    //estáticos
    companion object {
        lateinit var INSTANCE: MiApp
            private set

        val applicationContext: Context get() { return INSTANCE.applicationContext }
    }
}