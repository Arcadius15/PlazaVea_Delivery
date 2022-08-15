package edu.pe.idat.plazaveadelivery.views

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pe.idat.plazaveadelivery.R
import edu.pe.idat.plazaveadelivery.databinding.ActivityOrderDetailBinding
import edu.pe.idat.plazaveadelivery.db.entity.TokenEntity
import edu.pe.idat.plazaveadelivery.db.entity.UsuarioEntity
import edu.pe.idat.plazaveadelivery.retrofit.req.OrdenAsignarRequest
import edu.pe.idat.plazaveadelivery.retrofit.req.RepartidorIDRequest
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenRes
import edu.pe.idat.plazaveadelivery.retrofit.res.RepartidorOrdenRes
import edu.pe.idat.plazaveadelivery.utils.ResponseHttp
import edu.pe.idat.plazaveadelivery.viewmodel.OrdenViewModel
import edu.pe.idat.plazaveadelivery.viewmodel.TokenViewModel
import edu.pe.idat.plazaveadelivery.viewmodel.UsuarioRoomViewModel
import edu.pe.idat.plazaveadelivery.views.adapter.ProductosOrderAdapter
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var ordenResponse: OrdenRes
    private lateinit var usuario: UsuarioEntity
    private lateinit var token: TokenEntity
    private lateinit var ordenViewModel: OrdenViewModel
    private lateinit var usuarioRoomViewModel: UsuarioRoomViewModel
    private lateinit var tokenViewModel: TokenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ordenResponse = intent.getSerializableExtra("orden") as OrdenRes

        ordenViewModel = ViewModelProvider(this)[OrdenViewModel::class.java]
        usuarioRoomViewModel = ViewModelProvider(this)[UsuarioRoomViewModel::class.java]
        tokenViewModel = ViewModelProvider(this)[TokenViewModel::class.java]

        binding.rvProductosOrden.setHasFixedSize(true)
        binding.rvProductosOrden.layoutManager = LinearLayoutManager(this)

        cargarDatos()

        binding.btnaceptar.setOnClickListener(this)
        binding.btnGoBackOrden.setOnClickListener(this)

        ordenViewModel.responseHttp.observe(this) {
            obtenerRespuesta(it)
        }
    }

    private fun obtenerRespuesta(responseHttp: ResponseHttp) {
        if (responseHttp.isSuccess) {
            Toast.makeText(
                applicationContext,
                "Te asignaste a esta orden",
                Toast.LENGTH_LONG
            ).show()
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        } else {
            Toast.makeText(
                applicationContext,
                "Hubo un problema con el servicio",
                Toast.LENGTH_LONG
            ).show()
            ordenResponse.repartidor = null
        }
        binding.btnaceptar.isEnabled = true
        binding.btnGoBackOrden.isEnabled = true
    }

    private fun cargarDatos() {
        binding.tvNombreCliente.text = ordenResponse.cliente.idCliente

        val fc = Calendar.getInstance()
        val df1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        df1.timeZone = TimeZone.getTimeZone("America/Lima")
        fc.time = df1.parse(ordenResponse.fecha) as Date
        val df2 = SimpleDateFormat("yyyy-MM-dd")
        df2.timeZone = TimeZone.getTimeZone("America/Lima")
        binding.tvFechaCompra.text = df2.format(fc.time)

        when (ordenResponse.status) {
            "ENCAMINO" -> {
                binding.tvEstadoPedido.text = "En Camino"
                binding.tvEstadoPedido.setTextColor(ContextCompat.getColor(applicationContext,R.color.blue_700))
                fc.add(Calendar.DATE, 10)
                binding.tvEntregaEstimada.text = df2.format(fc.time)
            }
            "ENTREGADO" -> {
                binding.tvEstadoPedido.text = "Entregado"
                binding.tvEstadoPedido.setTextColor(ContextCompat.getColor(applicationContext,R.color.green_accent))
                binding.tvLabelEntregaFecha.text = getString(R.string.entregado_el)
                fc.time = df1.parse(ordenResponse.fechaEntrega) as Date
                binding.tvEntregaEstimada.text = df2.format(fc.time)
            }
            "CANCELADO" -> {
                binding.tvEstadoPedido.text = "Cancelado"
                binding.tvEstadoPedido.setTextColor(ContextCompat.getColor(applicationContext,R.color.granate_700))
                binding.tvLabelEntregaFecha.text = getString(R.string.cancelado_el)
                fc.time = df1.parse(ordenResponse.fechaEntrega) as Date
                binding.tvEntregaEstimada.text = df2.format(fc.time)
            }
        }

        binding.tvDetDireccion.text = ordenResponse.direccion

        binding.rvProductosOrden.adapter = ProductosOrderAdapter(ordenResponse.ordendetalle)

        binding.tvDetMonto.text = "S/${String.format("%.2f",ordenResponse.monto)}"
        binding.tvDetIGV.text = "S/${String.format("%.2f",ordenResponse.igv)}"
        binding.tvDetTotal.text = "S/${String.format("%.2f",ordenResponse.total)}"

        if (ordenResponse.repartidor != null) {
            binding.btnaceptar.isEnabled = false
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btnaceptar -> getUserFromDB()
            R.id.btnGoBackOrden -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun getUserFromDB(){
        usuarioRoomViewModel.obtener().observe(this){
            ue -> ue?.let {
                usuario = ue
                getTokenFromDB()
            }
        }
    }

    private fun getTokenFromDB(){
        tokenViewModel.obtener().observe(this){
            te -> te?.let {
                token = te
                aceptarOrden()
            }
        }
    }

    private fun aceptarOrden() {
        binding.btnaceptar.isEnabled = false
        binding.btnGoBackOrden.isEnabled = false

        AlertDialog.Builder(this)
            .setTitle("Asignarse")
            .setMessage("¿Seguro que desea asignarse a este delivery?")
            .setPositiveButton("Sí") { dialogInterface, _ ->
                val repartidorIDRequest = RepartidorIDRequest(
                    usuario.idRepartidor
                )

                val ordenAsignarRequest = OrdenAsignarRequest(
                    repartidorIDRequest
                )

                ordenResponse.repartidor = RepartidorOrdenRes(
                    usuario.idRepartidor
                )

                ordenViewModel.asignarseOrden(ordenResponse.idOrden, ordenAsignarRequest,
                                        "Bearer ${token.token}")

                dialogInterface.cancel()
            }
            .setNegativeButton("No"){ dialogInterface, _ ->
                binding.btnaceptar.isEnabled = true
                binding.btnGoBackOrden.isEnabled = true
                dialogInterface.cancel()
            }
            .show()
    }
}