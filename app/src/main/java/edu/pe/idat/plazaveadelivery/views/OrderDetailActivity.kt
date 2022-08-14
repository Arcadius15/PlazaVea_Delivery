package edu.pe.idat.plazaveadelivery.views

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pe.idat.plazaveadelivery.R
import edu.pe.idat.plazaveadelivery.databinding.ActivityOrderDetailBinding
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenRes
import edu.pe.idat.plazaveadelivery.utils.ResponseHttp
import edu.pe.idat.plazaveadelivery.viewmodel.OrdenViewModel
import edu.pe.idat.plazaveadelivery.views.adapter.ProductosOrderAdapter
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var ordenResponse: OrdenRes
    private lateinit var ordenViewModel: OrdenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ordenResponse = intent.getSerializableExtra("orden") as OrdenRes

        ordenViewModel = ViewModelProvider(this)[OrdenViewModel::class.java]

        binding.rvProductosOrden.setHasFixedSize(true)
        binding.rvProductosOrden.layoutManager = LinearLayoutManager(this)

        cargarDatos()

        ordenViewModel.responseHttp.observe(this) {
            obtenerRespuesta(it)
        }

    }

    private fun obtenerRespuesta(responseHttp: ResponseHttp) {
        if (responseHttp.isSuccess) {
            Toast.makeText(
                applicationContext,
                "Compra Cancelada",
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
            ordenResponse.status = "ENCAMINO"
        }
        binding.btncancelar.isEnabled = true
        binding.btnGoBackOrden.isEnabled = true
    }

    private fun cargarDatos() {
        val fc = Calendar.getInstance()
        val df1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        df1.timeZone = TimeZone.getTimeZone("America/Lima")
        fc.time = df1.parse(ordenResponse.fecha) as Date
        val df2 = SimpleDateFormat("yyyy-MM-dd")
        df2.timeZone = TimeZone.getTimeZone("America/Lima")
        binding.tvFechaCompra.text = df2.format(fc.time)

        binding.tvDetDireccion.text = ordenResponse.direccion

        binding.rvProductosOrden.adapter = ProductosOrderAdapter(ordenResponse.ordendetalle)

        binding.tvDetMonto.text = "S/${String.format("%.2f",ordenResponse.monto)}"
        binding.tvDetIGV.text = "S/${String.format("%.2f",ordenResponse.igv)}"
        binding.tvDetTotal.text = "S/${String.format("%.2f",ordenResponse.total)}"

    }
}