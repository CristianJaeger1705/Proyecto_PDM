package sv.edu.ues.fia.proyecto_pdm.reparacion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sv.edu.ues.fia.proyecto_pdm.R

class ReparacionAdapter(
    private var reparaciones: List<Reparacion>,
    private val onItemClick: (Reparacion) -> Unit
) : RecyclerView.Adapter<ReparacionAdapter.ReparacionViewHolder>() {

    class ReparacionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDescripcion: TextView = view.findViewById(R.id.txtItemRepDescripcion)
        val txtId: TextView = view.findViewById(R.id.txtItemRepId)
        val txtDetalles: TextView = view.findViewById(R.id.txtItemRepDetalles)
        val txtFecha: TextView = view.findViewById(R.id.txtItemRepFecha)
        val txtCosto: TextView = view.findViewById(R.id.txtItemRepCosto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReparacionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reparacion, parent, false)
        return ReparacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReparacionViewHolder, position: Int) {
        val rep = reparaciones[position]
        val context = holder.itemView.context
        holder.txtDescripcion.text = rep.descripcionTrabajo
        holder.txtId.text = context.getString(R.string.id_label, rep.idReparacion)
        holder.txtDetalles.text = context.getString(R.string.rep_detalles, rep.idTaller, rep.idVehiculo)
        holder.txtFecha.text = context.getString(R.string.rep_fecha, rep.fechaEntrada)
        holder.txtCosto.text = context.getString(R.string.rep_costo, rep.costo)

        holder.itemView.setOnClickListener { onItemClick(rep) }
    }

    override fun getItemCount() = reparaciones.size

    fun updateList(newList: List<Reparacion>) {
        reparaciones = newList
        notifyDataSetChanged()
    }
}
