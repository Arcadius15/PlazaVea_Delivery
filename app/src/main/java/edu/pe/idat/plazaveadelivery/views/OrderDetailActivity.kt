package edu.pe.idat.plazaveadelivery.views

import android.app.Activity
import android.content.Intent
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
import edu.pe.idat.plazaveadelivery.retrofit.req.OrdenPatchReq
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenRes
import edu.pe.idat.plazaveadelivery.retrofit.res.RepartidorOrdenRes
import edu.pe.idat.plazaveadelivery.utils.Mensaje
import edu.pe.idat.plazaveadelivery.utils.ResponseHttp
import edu.pe.idat.plazaveadelivery.utils.TipoMensaje
import edu.pe.idat.plazaveadelivery.viewmodel.ClienteViewModel
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
    private lateinit var clienteViewModel: ClienteViewModel
    private lateinit var usuarioRoomViewModel: UsuarioRoomViewModel
    private lateinit var tokenViewModel: TokenViewModel

    private var mensaje = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ordenResponse = intent.getSerializableExtra("orden") as OrdenRes

        ordenViewModel = ViewModelProvider(this)[OrdenViewModel::class.java]
        clienteViewModel = ViewModelProvider(this)[ClienteViewModel::class.java]
        usuarioRoomViewModel = ViewModelProvider(this)[UsuarioRoomViewModel::class.java]
        tokenViewModel = ViewModelProvider(this)[TokenViewModel::class.java]

        binding.rvProductosOrden.setHasFixedSize(true)
        binding.rvProductosOrden.layoutManager = LinearLayoutManager(this)

        binding.btnmarcarentregado.visibility = View.GONE
        binding.llorden.visibility = View.GONE

        cargarDatos()

        binding.btnaceptar.setOnClickListener(this)
        binding.btnmarcarentregado.setOnClickListener(this)
        binding.btnvernmapa.setOnClickListener(this)
        binding.btnGoBackOrden.setOnClickListener(this)

        ordenViewModel.responseHttp.observe(this) {
            obtenerRespuesta(it)
        }
    }

    private fun obtenerRespuesta(responseHttp: ResponseHttp) {
        if (responseHttp.isSuccess) {
            Toast.makeText(
                applicationContext,
                mensaje,
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
        getTokenFromDB("c")

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
                binding.btnaceptar.isEnabled = false
                binding.btnmarcarentregado.isEnabled = false
            }
            "CANCELADO" -> {
                binding.tvEstadoPedido.text = "Cancelado"
                binding.tvEstadoPedido.setTextColor(ContextCompat.getColor(applicationContext,R.color.granate_700))
                binding.tvLabelEntregaFecha.text = getString(R.string.cancelado_el)
                fc.time = df1.parse(ordenResponse.fechaEntrega) as Date
                binding.tvEntregaEstimada.text = df2.format(fc.time)
                binding.btnaceptar.isEnabled = false
                binding.btnmarcarentregado.isEnabled = false
            }
        }

        binding.tvDetDireccion.text = ordenResponse.direccion

        binding.rvProductosOrden.adapter = ProductosOrderAdapter(ordenResponse.ordendetalle)

        binding.tvDetMonto.text = "S/${String.format("%.2f",ordenResponse.monto)}"
        binding.tvDetIGV.text = "S/${String.format("%.2f",ordenResponse.igv)}"
        binding.tvDetTotal.text = "S/${String.format("%.2f",ordenResponse.total)}"

        if (ordenResponse.repartidor != null) {
            binding.btnaceptar.isEnabled = false
            binding.btnaceptar.visibility = View.GONE
            binding.btnmarcarentregado.visibility = View.VISIBLE
            binding.tvTituloOD.text = "Gestionar Entrega"
        } else {
            binding.btnvernmapa.visibility = View.GONE
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btnaceptar -> getUserFromDB()
            R.id.btnmarcarentregado -> getTokenFromDB("m")
            R.id.btnvernmapa -> if (ordenResponse.lat != null || ordenResponse.lng != null) {
                startActivity(
                    Intent(this, UbicacionActivity::class.java)
                        .putExtra("lat", ordenResponse.lat.toString())
                        .putExtra("lng", ordenResponse.lng.toString()),
                )
            } else {
                Mensaje.enviarMensaje(binding.root,"Esta orden no incluye información de localización",TipoMensaje.ERROR)
            }
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
                getTokenFromDB("a")
            }
        }
    }

    private fun getTokenFromDB(accion: String){
        tokenViewModel.obtener().observe(this){
            te -> te?.let {
                token = te
                when (accion) {
                    "a" -> aceptarOrden()
                    "m" -> marcarEntregado()
                    "c" -> cargarCliente()
                }
            }
        }
    }

    private fun marcarEntregado() {
        binding.btnmarcarentregado.isEnabled = false
        binding.btnGoBackOrden.isEnabled = false

        AlertDialog.Builder(this)
            .setTitle("Entregar")
            .setMessage("¿Seguro que desea marcar este delivery como entregado?")
            .setPositiveButton("Sí") { dialogInterface, _ ->
                val fechaActual = Calendar.getInstance()
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                df.timeZone = TimeZone.getTimeZone("America/Lima")

                val ordenPatchReq = OrdenPatchReq(
                    df.format(fechaActual.time),
                    "ENTREGADO"
                )

                ordenResponse.status = "ENTREGADO"
                ordenResponse.fechaEntrega = df.format(fechaActual.time)

                ordenViewModel.actualizarOrden(ordenResponse.idOrden, ordenPatchReq,
                    "Bearer ${token.token}")

                mensaje = "Marcaste este pedido como Entregado"

                dialogInterface.cancel()
            }
            .setNegativeButton("No"){ dialogInterface, _ ->
                binding.btnmarcarentregado.isEnabled = true
                binding.btnGoBackOrden.isEnabled = true
                dialogInterface.cancel()
            }
            .show()
    }

    private fun cargarCliente() {
        clienteViewModel.findById(ordenResponse.cliente.idCliente,
                                  "Bearer ${token.token}")
            .observe(this){
                binding.tvNombreCliente.text = "${it.nombre} ${it.apellidos}"
                binding.llorden.visibility = View.VISIBLE
                binding.progressbarDetOrden.visibility = View.GONE
            }
    }

    private fun aceptarOrden() {
        binding.btnaceptar.isEnabled = false
        binding.btnGoBackOrden.isEnabled = false

        AlertDialog.Builder(this)
            .setTitle("Asignarse")
            .setMessage("¿Seguro que desea asignarse a este delivery?")
            .setPositiveButton("Sí") { dialogInterface, _ ->
                ordenResponse.repartidor = RepartidorOrdenRes(
                    usuario.idRepartidor
                )

                ordenViewModel.asignarseOrden(ordenResponse.idOrden, usuario.idRepartidor,
                                        "Bearer ${token.token}")

                mensaje = "Te asignaste a esta orden"

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