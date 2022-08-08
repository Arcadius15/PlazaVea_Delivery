package edu.pe.idat.plazaveadelivery.utils

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import edu.pe.idat.plazaveadelivery.R

object Mensaje {
    fun enviarMensaje(vista: View, mensaje: String, tipo: TipoMensaje){
        val snackbar = Snackbar.make(vista, mensaje, Snackbar.LENGTH_LONG)
        val snackbarView: View = snackbar.view
        if(tipo == TipoMensaje.ERROR){
            snackbarView.setBackgroundColor(
                ContextCompat.getColor(
                    MiApp.INSTANCE,
                    R.color.snackbarerror
                )
            )
        }else{
            snackbarView.setBackgroundColor(
                ContextCompat.getColor(
                    MiApp.INSTANCE,
                    R.color.snackbarexito
                )
            )
        }
        snackbar.show()
    }
}