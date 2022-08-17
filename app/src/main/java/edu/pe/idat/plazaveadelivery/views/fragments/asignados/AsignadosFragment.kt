package edu.pe.idat.plazaveadelivery.views.fragments.asignados

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pe.idat.plazaveadelivery.databinding.FragmentAsignadosBinding
import edu.pe.idat.plazaveadelivery.db.entity.TokenEntity
import edu.pe.idat.plazaveadelivery.db.entity.UsuarioEntity
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenPageRes
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenRes
import edu.pe.idat.plazaveadelivery.viewmodel.OrdenViewModel
import edu.pe.idat.plazaveadelivery.viewmodel.TokenViewModel
import edu.pe.idat.plazaveadelivery.viewmodel.UsuarioRoomViewModel
import edu.pe.idat.plazaveadelivery.views.OrderDetailActivity
import edu.pe.idat.plazaveadelivery.views.adapter.OrdenAdapter

class AsignadosFragment : Fragment(), OrdenAdapter.IOrdenAdapter {

    private var _binding: FragmentAsignadosBinding? = null
    private val binding get() = _binding!!

    private lateinit var ordenViewModel: OrdenViewModel
    private lateinit var usuarioRoomViewModel: UsuarioRoomViewModel
    private lateinit var tokenViewModel: TokenViewModel

    private lateinit var usuario: UsuarioEntity
    private lateinit var token: TokenEntity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAsignadosBinding.inflate(inflater, container, false)

        ordenViewModel = ViewModelProvider(requireActivity())[OrdenViewModel::class.java]
        usuarioRoomViewModel = ViewModelProvider(requireActivity())[UsuarioRoomViewModel::class.java]
        tokenViewModel = ViewModelProvider(requireActivity())[TokenViewModel::class.java]

        binding.tvMensajeSinAsignados.visibility = View.GONE

        binding.rvOrdenesAsignadas.setHasFixedSize(true)
        binding.rvOrdenesAsignadas.layoutManager = LinearLayoutManager(requireActivity())

        getUserFromDB()

        return binding.root
    }

    private fun getTotalElements() {
        ordenViewModel.getListTotalElements(usuario.idTienda, "Bearer ${token.token}").observe(viewLifecycleOwner){
            if (it != null) {
                getOrdenes(it.totalElements)
            } else {
                binding.progressbarAsignados.visibility = View.GONE
                binding.tvMensajeSinAsignados.visibility = View.VISIBLE
            }
        }
    }

    private fun getOrdenes(size: Int) {
        ordenViewModel.getOrdenesByTienda(usuario.idTienda,size,
            "Bearer ${token.token}").observe(viewLifecycleOwner){
                procesarDatos(it)
        }
    }

    private fun procesarDatos(ordenPageRes: OrdenPageRes?) {
        if (ordenPageRes != null) {
            val ordenesFiltradas = ArrayList<OrdenRes>()

            ordenPageRes.content.forEach {
                if (it.repartidor != null && it.repartidor!!.idRepartidor == usuario.idRepartidor) {
                    ordenesFiltradas.add(it)
                }
            }

            if (ordenesFiltradas.size > 0) {
                binding.rvOrdenesAsignadas.adapter = OrdenAdapter(ArrayList(ordenesFiltradas.sortedWith(
                    compareBy{ or ->
                        or.idOrden
                    })),this)
                binding.progressbarAsignados.visibility = View.GONE
            } else {
                binding.progressbarAsignados.visibility = View.GONE
                binding.tvMensajeSinAsignados.visibility = View.VISIBLE
            }
        } else {
            binding.progressbarAsignados.visibility = View.GONE
            binding.tvMensajeSinAsignados.visibility = View.VISIBLE
        }
    }

    private fun getUserFromDB(){
        usuarioRoomViewModel.obtener().observe(viewLifecycleOwner){
            ue -> ue?.let {
                usuario = ue
                getTokenFromDB()
            }
        }
    }

    private fun getTokenFromDB(){
        tokenViewModel.obtener().observe(viewLifecycleOwner){
            te -> te?.let {
                token = te
                getTotalElements()
            }
        }
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                parentFragmentManager.beginTransaction().detach(this).commitNow()
                parentFragmentManager.beginTransaction().attach(this).commitNow()
            } else {
                parentFragmentManager.beginTransaction().detach(this).attach(this).commit()
            }
        }
    }

    override fun goToDetail(ordenRes: OrdenRes) {
        launcher.launch(Intent(requireActivity(), OrderDetailActivity::class.java)
                .putExtra("orden",ordenRes))
    }

}