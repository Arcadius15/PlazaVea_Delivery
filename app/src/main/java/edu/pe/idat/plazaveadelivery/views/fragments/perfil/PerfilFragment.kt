package edu.pe.idat.plazaveadelivery.views.fragments.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.pe.idat.plazaveadelivery.databinding.FragmentPerfilBinding
import edu.pe.idat.plazaveadelivery.db.entity.UsuarioEntity
import edu.pe.idat.plazaveadelivery.viewmodel.UsuarioRoomViewModel

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private lateinit var usuarioRoomViewModel: UsuarioRoomViewModel

    private lateinit var usuario: UsuarioEntity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)

        usuarioRoomViewModel = ViewModelProvider(requireActivity())[UsuarioRoomViewModel::class.java]

        getUserFromDB()

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

    }

}