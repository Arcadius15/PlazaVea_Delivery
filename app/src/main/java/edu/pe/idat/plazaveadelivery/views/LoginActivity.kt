package edu.pe.idat.plazaveadelivery.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
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

        binding.btnlogin.setOnClickListener(this)
        authViewModel = ViewModelProvider(this)
            .get(AuthViewModel::class.java)
        authViewModel.responseLogin.observe(this, Observer {
            obtenerTokenLogin(it)
        })
    }

    private fun obtenerTokenLogin(it: LoginRes?) {
        if (it!=null){
            SharedPrefCons(this).save("token",it.token)
            startActivity(Intent(applicationContext,MainActivity::class.java))
            finish()
        }else{
            Mensaje.enviarMensaje(binding.root,"Error",TipoMensaje.ERROR)
            binding.btnlogin.isEnabled=true
        }


    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.btnlogin-> autenticar()
        }
    }

    private fun autenticar() {
        binding.btnlogin.isEnabled = false
        var okLogin = true
        if (binding.etusuario.text.toString().trim().isEmpty()){
            binding.etusuario.isFocusableInTouchMode = true
            binding.etusuario.requestFocus()
            okLogin = false
        }else if (binding.etpassword.text.toString().trim().isEmpty()) {
            binding.etpassword.isFocusableInTouchMode = true
            binding.etpassword.requestFocus()
            okLogin = false
        }
        if (okLogin){
            authViewModel.autenticarUsuario(
                binding.etusuario.text.toString(),
                binding.etpassword.text.toString())
        }else{
            binding.btnlogin.isEnabled = true
            Mensaje.enviarMensaje(binding.root,"Ingrese su Usuario o Password",TipoMensaje.ERROR)
        }
    }
}