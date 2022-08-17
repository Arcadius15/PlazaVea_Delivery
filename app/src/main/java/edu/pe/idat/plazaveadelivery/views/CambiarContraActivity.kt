package edu.pe.idat.plazaveadelivery.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import edu.pe.idat.plazaveadelivery.R
import edu.pe.idat.plazaveadelivery.databinding.ActivityCambiarContraBinding
import edu.pe.idat.plazaveadelivery.db.entity.UsuarioEntity
import edu.pe.idat.plazaveadelivery.retrofit.req.UsuarioPwsReq
import edu.pe.idat.plazaveadelivery.retrofit.res.MensajeRes
import edu.pe.idat.plazaveadelivery.viewmodel.AuthViewModel
import edu.pe.idat.plazaveadelivery.viewmodel.UsuarioRoomViewModel

class CambiarContraActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCambiarContraBinding

    private lateinit var authViewModel: AuthViewModel
    private lateinit var usuarioRoomViewModel: UsuarioRoomViewModel

    private lateinit var usuario: UsuarioEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCambiarContraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        usuarioRoomViewModel = ViewModelProvider(this)[UsuarioRoomViewModel::class.java]

        binding.btnGoBackHome.setOnClickListener(this)
        binding.btnCambiar.setOnClickListener(this)

        authViewModel.mensajeRes.observe(this){
            try {
                obtenerMensaje(it!!)
            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext,
                    "La contraseña actual ingresada es incorrecta",
                    Toast.LENGTH_LONG
                ).show()
                binding.btnGoBackHome.isEnabled = true
                binding.btnCambiar.isEnabled = true
            }
        }
    }

    private fun obtenerMensaje(mensaje: MensajeRes) {
        when (mensaje.mensaje) {
            "Contraseña Actualizada exitosamente" -> {
                Toast.makeText(
                    applicationContext,
                    mensaje.mensaje,
                    Toast.LENGTH_LONG
                ).show()
                gotoHome()
            }
            "Contraseña Invalida" -> {
                Toast.makeText(
                    applicationContext,
                    "La contraseña actual ingresada es incorrecta",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                Toast.makeText(
                    applicationContext,
                    "Error: ${mensaje.mensaje}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.btnGoBackHome.isEnabled = true
        binding.btnCambiar.isEnabled = true
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.btnGoBackHome -> gotoHome()
            R.id.btn_cambiar -> getUserFromDB()
        }
    }

    private fun cambiarContra() {
        binding.btnGoBackHome.isEnabled = false
        binding.btnCambiar.isEnabled = false
        if (validarCampos()){
            val usuarioPswRequest = UsuarioPwsReq(
                usuario.email,
                binding.edtNewPassword.text.toString().trim(),
                binding.edtOldPassword.text.toString().trim()
            )

            authViewModel.editPassword(usuarioPswRequest)
        } else {
            binding.btnGoBackHome.isEnabled = true
            binding.btnCambiar.isEnabled = true
        }
    }

    private fun validarCampos(): Boolean {
        if (binding.edtOldPassword.text.toString().isBlank()) {
            Toast.makeText(
                applicationContext,
                "Debe ingresar su contraseña actual.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (binding.edtNewPassword.text.toString().isBlank()) {
            Toast.makeText(
                applicationContext,
                "Debe ingresar una nueva contraseña.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (binding.edtPasswordConf.text.toString().isBlank() ||
            binding.edtPasswordConf.text.toString() != binding.edtNewPassword.text.toString()) {
            Toast.makeText(
                applicationContext,
                "Las contraseñas no coinciden.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }

    private fun gotoHome(){
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun getUserFromDB(){
        usuarioRoomViewModel.obtener().observe(this){
            usuario = it
            cambiarContra()
        }
    }
}