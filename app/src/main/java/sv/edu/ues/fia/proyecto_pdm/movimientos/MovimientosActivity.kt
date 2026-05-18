package sv.edu.ues.fia.proyecto_pdm.movimientos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.transporte.GestionTransporteActivity

class MovimientosActivity : BaseActivity() {

    private lateinit var handler: MovimientoHandler
    private lateinit var listView: ListView
    private lateinit var layoutPendientes: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimientos)

        handler = MovimientoHandler(this)
        listView = findViewById(R.id.listMovimientosPendientes)
        layoutPendientes = findViewById(R.id.layoutPendientes)

        val btnOpcionMedios = findViewById<ImageButton>(R.id.btnOpcionMedios)
        val btnOpcionMovimientos = findViewById<ImageButton>(R.id.btnOpcionMovimientos)

        btnOpcionMedios.setOnClickListener {
            val intent = Intent(this, GestionTransporteActivity::class.java)
            startActivity(intent)
        }

        btnOpcionMovimientos.setOnClickListener {
            val intent = Intent(this, GestionMovimientosActivity::class.java)
            startActivity(intent)
        }

        actualizarListaPendientes()
    }

    override fun onResume() {
        super.onResume()
        actualizarListaPendientes()
    }

    private fun actualizarListaPendientes() {
        val sharedPref = getSharedPreferences("nombre_usuario", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "") ?: ""

        if (username == "admin") {
            val pendientes = handler.obtenerNoAutorizados()
            if (pendientes.isNotEmpty()) {
                layoutPendientes.visibility = View.VISIBLE
                val adapter = object : ArrayAdapter<Movimiento>(this, android.R.layout.simple_list_item_2, android.R.id.text1, pendientes) {
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = super.getView(position, convertView, parent)
                        val mov = getItem(position)!!
                        val text1 = view.findViewById<TextView>(android.R.id.text1)
                        val text2 = view.findViewById<TextView>(android.R.id.text2)
                        
                        text1.text = getString(R.string.label_movement_item, mov.idMovimiento, mov.tipoMovimiento)
                        text2.text = getString(R.string.label_movement_details, mov.fecha, mov.hora, mov.observaciones)
                        
                        view.setOnClickListener {
                            AlertDialog.Builder(context)
                                .setTitle(getString(R.string.title_authorize_movement))
                                .setMessage(getString(R.string.msg_authorize_confirm, mov.idMovimiento))
                                .setPositiveButton(getString(R.string.btn_authorize)) { _, _ ->
                                    if (handler.autorizar(mov.idMovimiento) > 0) {
                                        Toast.makeText(context, getString(R.string.msg_movement_authorized), Toast.LENGTH_SHORT).show()
                                        actualizarListaPendientes()
                                    }
                                }
                                .setNegativeButton(getString(R.string.btn_cancel), null)
                                .show()
                        }
                        return view
                    }
                }
                listView.adapter = adapter
            } else {
                layoutPendientes.visibility = View.GONE
            }
        } else {
            layoutPendientes.visibility = View.GONE
        }
    }
}
