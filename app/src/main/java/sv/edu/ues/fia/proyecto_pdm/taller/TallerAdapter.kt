package sv.edu.ues.fia.proyecto_pdm.taller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sv.edu.ues.fia.proyecto_pdm.R

class TallerAdapter(
    private var talleres: List<Taller>,
    private val onItemClick: (Taller) -> Unit
) : RecyclerView.Adapter<TallerAdapter.TallerViewHolder>() {

    class TallerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtItemNombre)
        val txtId: TextView = view.findViewById(R.id.txtItemId)
        val txtDireccion: TextView = view.findViewById(R.id.txtItemDireccion)
        val txtTelefono: TextView = view.findViewById(R.id.txtItemTelefono)
        val txtAutorizado: TextView = view.findViewById(R.id.txtItemAutorizado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TallerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_taller, parent, false)
        return TallerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TallerViewHolder, position: Int) {
        val taller = talleres[position]
        holder.txtNombre.text = taller.nombreTaller
        holder.txtId.text = holder.itemView.context.getString(R.string.id_label, taller.idTaller)
        holder.txtDireccion.text = taller.direccion
        holder.txtTelefono.text = taller.telefono

        if (taller.autorizado?.trim()?.uppercase() == "S") {
            holder.txtAutorizado.visibility = View.VISIBLE
        } else {
            holder.txtAutorizado.visibility = View.GONE
        }

        holder.itemView.setOnClickListener { onItemClick(taller) }
    }

    override fun getItemCount() = talleres.size

    fun updateList(newList: List<Taller>) {
        talleres = newList
        notifyDataSetChanged()
    }
}
