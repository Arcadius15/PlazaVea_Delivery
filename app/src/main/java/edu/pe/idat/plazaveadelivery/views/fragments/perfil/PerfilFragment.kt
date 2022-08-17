package edu.pe.idat.plazaveadelivery.views.fragments.perfil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.pe.idat.plazaveadelivery.databinding.FragmentPerfilBinding
import edu.pe.idat.plazaveadelivery.db.entity.UsuarioEntity
import edu.pe.idat.plazaveadelivery.utils.SharedPrefCons
import edu.pe.idat.plazaveadelivery.viewmodel.AuthViewModel
import edu.pe.idat.plazaveadelivery.viewmodel.UsuarioRoomViewModel
import edu.pe.idat.plazaveadelivery.views.CambiarContraActivity
import edu.pe.idat.plazaveadelivery.views.LoginActivity

class PerfilFragment : Fragment(){

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private lateinit var usuarioRoomViewModel: UsuarioRoomViewModel
    private lateinit var authViewModel: AuthViewModel

    private lateinit var usuario: UsuarioEntity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)

        usuarioRoomViewModel = ViewModelProvider(requireActivity())[UsuarioRoomViewModel::class.java]
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        binding.llperfil.visibility = View.GONE

        getUserFromDB()

        binding.btnlogout.setOnClickListener{logout()}
        binding.btnircambiarcontra.setOnClickListener{startActivity(
            Intent(requireActivity(),CambiarContraActivity::class.java)
        )}

        return binding.root
    }

    private fun getUserFromDB(){
        usuarioRoomViewModel.obtener().observe(viewLifecycleOwner){
            ue -> ue?.let {
                usuario = ue
                cargarDatos()
            }
        }
    }

    private fun cargarDatos() {
        binding.tvCorreo.text = usuario.email
        binding.tvNombreRepartidor.text = usuario.nombre
        binding.tvApellidoRepartidor.text = usuario.apellidos
        binding.tvdni.text = usuario.dni
        binding.tvplaca.text = usuario.placa
        authViewModel.getTiendaDelUsuario(usuario.idTienda).observe(viewLifecycleOwner){
            binding.tvtienda.text = it.nombre
            binding.llperfil.visibility = View.VISIBLE
            binding.progressbarPerfil.visibility = View.GONE
        }
    }

    private fun logout() {
       val sharedPref = SharedPrefCons(requireActivity())
        if(sharedPref.getSomeBooleanValue("mantener")){
            sharedPref.remove("mantener")
        }

        val i = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(i)
        requireActivity().finish()
    }

}