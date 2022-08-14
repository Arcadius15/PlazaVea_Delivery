package edu.pe.idat.plazaveadelivery.views.fragments.pendientes

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pe.idat.plazaveadelivery.databinding.FragmentPendientesBinding
import edu.pe.idat.plazaveadelivery.db.entity.TokenEntity
import edu.pe.idat.plazaveadelivery.db.entity.UsuarioEntity
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenPageRes
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenRes
import edu.pe.idat.plazaveadelivery.viewmodel.OrdenViewModel
import edu.pe.idat.plazaveadelivery.viewmodel.TokenViewModel
import edu.pe.idat.plazaveadelivery.viewmodel.UsuarioRoomViewModel
import edu.pe.idat.plazaveadelivery.views.OrderDetailActivity
import edu.pe.idat.plazaveadelivery.views.adapter.OrdenAdapter

class PendientesFragment : Fragment(), OrdenAdapter.IOrdenAdapter {

    private var _binding: FragmentPendientesBinding? = null
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
        _binding = FragmentPendientesBinding.inflate(inflater, container, false)

        ordenViewModel = ViewModelProvider(requireActivity())[OrdenViewModel::class.java]
        usuarioRoomViewModel = ViewModelProvider(requireActivity())[UsuarioRoomViewModel::class.java]
        tokenViewModel = ViewModelProvider(requireActivity())[TokenViewModel::class.java]

        binding.rvOrdenesPendientes.setHasFixedSize(true)
        binding.rvOrdenesPendientes.layoutManager = LinearLayoutManager(requireActivity())

        getUserFromDB()

        return binding.root
    }

    private fun getOrdenes() {
        ordenViewModel.getOrdenesByTienda(usuario.idTienda,1,
                                        "Bearer ${token.token}").observe(viewLifecycleOwner){
                                            procesarDatos(it)
        }
    }

    private fun procesarDatos(ordenPageRes: OrdenPageRes?) {
        if (ordenPageRes != null) {
            val paginas = (1..ordenPageRes.totalPages).toList()
            val adapterPaginas = ArrayAdapter(requireActivity(),androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                              paginas)
            binding.spPagina.adapter = adapterPaginas

            val ordenesFiltradas = ArrayList<OrdenRes>()

            ordenPageRes.content.forEach {
                if (it.repartidor == null && it.status == "ENCAMINO") {
                    ordenesFiltradas.add(it)
                }
            }

            binding.rvOrdenesPendientes.adapter = OrdenAdapter(ArrayList(ordenesFiltradas.sortedWith(
                compareBy{ or ->
                    or.idOrden
                })),this)
        } else {
            //añadir progress bar
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
                getOrdenes()
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
        launcher.launch(
            Intent(requireActivity(),OrderDetailActivity::class.java)
            .putExtra("orden",ordenRes))
    }
}