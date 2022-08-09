package edu.pe.idat.plazaveadelivery.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.pe.idat.plazaveadelivery.R
import edu.pe.idat.plazaveadelivery.databinding.ActivityLoginBinding
import edu.pe.idat.plazaveadelivery.retrofit.res.LoginRes
import edu.pe.idat.plazaveadelivery.utils.Mensaje
import edu.pe.idat.plazaveadelivery.utils.SharedPrefCons
import edu.pe.idat.plazaveadelivery.utils.TipoMensaje
import edu.pe.idat.plazaveadelivery.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(this)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        authViewModel.responseLogin.observe(this) {
            obtenerTokenLogin(it)
        }
    }

    private fun obtenerTokenLogin(it: LoginRes?) {
        if (it!=null){
            SharedPrefCons(this).save("token",it.token)
            startActivity(Intent(applicationContext,MainActivity::class.java))
            finish()
        }else{
            Mensaje.enviarMensaje(binding.root,"Error",TipoMensaje.ERROR)
            binding.btnLogin.isEnabled=true
        }
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.btn_login-> autenticar()
        }
    }

    private fun autenticar() {
        binding.btnLogin.isEnabled = false
        var okLogin = true
        if (binding.edTextEmail.text.toString().trim().isEmpty()){
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
                binding.edTextEmail.text.toString(),
                binding.edTextPassword.text.toString())
        }else{
            binding.btnLogin.isEnabled = true
            Mensaje.enviarMensaje(binding.root,"Ingrese su Usuario o Password",TipoMensaje.ERROR)
        }
    }
}