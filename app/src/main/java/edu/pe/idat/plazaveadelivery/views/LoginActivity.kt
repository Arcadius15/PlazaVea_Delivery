package edu.pe.idat.plazaveadelivery.views

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.auth0.android.jwt.JWT
import edu.pe.idat.plazaveadelivery.R
import edu.pe.idat.plazaveadelivery.databinding.ActivityLoginBinding
import edu.pe.idat.plazaveadelivery.db.entity.TokenEntity
import edu.pe.idat.plazaveadelivery.db.entity.UsuarioEntity
import edu.pe.idat.plazaveadelivery.retrofit.res.LoginRes
import edu.pe.idat.plazaveadelivery.retrofit.res.UsuarioJWTResponse
import edu.pe.idat.plazaveadelivery.utils.Mensaje
import edu.pe.idat.plazaveadelivery.utils.SharedPrefCons
import edu.pe.idat.plazaveadelivery.utils.TipoMensaje
import edu.pe.idat.plazaveadelivery.viewmodel.AuthViewModel
import edu.pe.idat.plazaveadelivery.viewmodel.TokenViewModel
import edu.pe.idat.plazaveadelivery.viewmodel.UsuarioRoomViewModel

class LoginActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var tokenViewModel: TokenViewModel
    private lateinit var usuarioRoomViewModel: UsuarioRoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        tokenViewModel = ViewModelProvider(this)[TokenViewModel::class.java]
        usuarioRoomViewModel = ViewModelProvider(this)[UsuarioRoomViewModel::class.java]

        binding.btnLogin.setOnClickListener(this)

        authViewModel.responseLogin.observe(this) {
            obtenerTokenLogin(it)
        }

        checkUserSession()
    }

    private fun obtenerTokenLogin(it: LoginRes?) {
        if (it!=null){
            val tokenEntity = TokenEntity(
                it.token
            )

            val jwt = JWT(it.token)
            val sub = jwt.getClaim("sub").asString()
            val info = jwt.getClaim("info").asObject(UsuarioJWTResponse::class.java)

            val usuarioEntity = UsuarioEntity(
                info!!.idRepartidor,
                sub!!,
                info.nombre,
                info.apellidos,
                info.dni,
                info.direccion,
                info.numTelefonico,
                info.placa,
                info.idTienda
            )

            if (SharedPrefCons(this).getSomeBooleanValue("mantener")) {
                tokenViewModel.actualizar(tokenEntity)
                usuarioRoomViewModel.actualizar(usuarioEntity)
            } else {
                tokenViewModel.instertar(tokenEntity)
                usuarioRoomViewModel.insertar(usuarioEntity)
                if (binding.chkmantener.isChecked) {
                    SharedPrefCons(this).setSomeBooleanValue("mantener",true)
                }
            }

            Toast.makeText(
                applicationContext,
                "Sesión Iniciada",
                Toast.LENGTH_LONG
            ).show()

            startActivity(Intent(applicationContext,MainActivity::class.java))
            finish()
        }else{
            Mensaje.enviarMensaje(binding.root,"ERROR! Las credenciales son incorrectas",TipoMensaje.ERROR)
        }
        binding.btnLogin.isEnabled=true
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.btn_login -> autenticar()
        }
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun autenticar() {
        binding.btnLogin.isEnabled = false
        var okLogin = true
        if (binding.edTextEmail.text.toString().trim().isEmpty() ||
            !binding.edTextEmail.text.toString().trim().isEmailValid()){
            binding.edTextEmail.isFocusableInTouchMode = true
            binding.edTextEmail.requestFocus()
            okLogin = false
        }else if (binding.edTextPassword.text.toString().trim().isEmpty()) {
            binding.edTextPassword.isFocusableInTouchMode = true
            binding.edTextPassword.requestFocus()
            okLogin = false
        }
        if (okLogin){
            authViewModel.autenticarUsuario(
                binding.edTextEmail.text.toString().trim(),
                binding.edTextPassword.text.toString().trim())
        }else{
            binding.btnLogin.isEnabled = true
            Mensaje.enviarMensaje(binding.root,"Complete los campos e ingrese un correo válido",TipoMensaje.ERROR)
        }
    }

    private fun checkUserSession(){
        val sharedPref = SharedPrefCons(this)

        if (sharedPref.getSomeBooleanValue("mantener")) {
            usuarioRoomViewModel.obtener().observe(this){
                    usuario -> usuario?.let {
                    Toast.makeText(
                        applicationContext,
                        "Bienvenido de nuevo, ${usuario.nombre}.",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(applicationContext,MainActivity::class.java))
                    finish()
                }
            }
        } else {
            usuarioRoomViewModel.eliminarTodo()
            tokenViewModel.eliminarToken()
        }
    }
}