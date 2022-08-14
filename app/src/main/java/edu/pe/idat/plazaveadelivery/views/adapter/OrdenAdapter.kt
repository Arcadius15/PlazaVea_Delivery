package edu.pe.idat.plazaveadelivery.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import edu.pe.idat.plazaveadelivery.R
import edu.pe.idat.plazaveadelivery.databinding.CardviewPedidoBinding
import edu.pe.idat.plazaveadelivery.retrofit.res.OrdenRes
import java.text.SimpleDateFormat
import java.util.*

class OrdenAdapter(private var ordenes: ArrayList<OrdenRes>, private var listener: IOrdenAdapter): RecyclerView.Adapter<OrdenAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: CardviewPedidoBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardviewPedidoBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(ordenes[position]){
                binding.tvIdOrden.text = idOrden
                binding.tvDireccion.text = direccion
                val fc = Calendar.getInstance()
                val df1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                df1.timeZone = TimeZone.getTimeZone("America/Lima")
                fc.time = df1.parse(fecha) as Date
                val df2 = SimpleDateFormat("yyyy-MM-dd")
                df2.timeZone = TimeZone.getTimeZone("America/Lima")
                binding.tvFechaCompraCv.text = df2.format(fc.time)
                when (status) {
                    "ENCAMINO" -> {
                        binding.tvStatus.text = "En Camino"
                        binding.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.blue_700))
                        fc.add(Calendar.DATE, 10)
                        binding.tvFechaEstimadaCv.text = df2.format(fc.time)
                    }
                    "ENTREGADO" -> {
                        binding.tvStatus.text = "Entregado"
                        binding.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green_accent))
                        binding.tvEntregaEstimadaCv.text = holder.itemView.context.getString(R.string.entregado_el)
                        fc.time = df1.parse(fechaEntrega) as Date
                        binding.tvFechaEstimadaCv.text = df2.format(fc.time)
                    }
                    "CANCELADO" -> {
                        binding.tvStatus.text = "Cancelado"
                        binding.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.granate_700))
                        binding.tvEntregaEstimadaCv.text = holder.itemView.context.getString(R.string.cancelado_el)
                        fc.time = df1.parse(fechaEntrega) as Date
                        binding.tvFechaEstimadaCv.text = df2.format(fc.time)
                    }
                }
                if (repartidor != null) {
                    binding.ivPedido.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.green_accent), android.graphics.PorterDuff.Mode.SRC_IN)
                    binding.tvAsignado.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green_accent))
                    binding.tvAsignado.text = holder.itemView.context.getString(R.string.te_asignaste)
                }
                holder.itemView.setOnClickListener{listener.goToDetail(this)}
            }
        }
    }

    override fun getItemCount() = ordenes.size

    interface IOrdenAdapter {
        fun goToDetail(ordenRes: OrdenRes)
    }
}