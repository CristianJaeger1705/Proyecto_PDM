package sv.edu.ues.fia.proyecto_pdm.transporte

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R

class GestionTransporteActivity : BaseActivity() {

    private lateinit var handler: MedioTransporteHandler
    private var medioActual: MedioTransporte? = null
    private lateinit var medios: List<MedioTransporte>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_transporte)

        handler = MedioTransporteHandler(this)

        val spinnerMedios = findViewById<Spinner>(R.id.spinnerGestionMedioId)
        val editTipo = findViewById<EditText>(R.id.editTipo)
        val editCapacidad = findViewById<EditText>(R.id.editCapacidad)

        val btnIrACrear = findViewById<Button>(R.id.btnIrACrear)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)

        // Validar permisos (Prefix 05)
        if (!tienePermiso("051")) btnIrACrear.visibility = View.GONE
        if (!tienePermiso("052")) btnActualizar.visibility = View.GONE
        if (!tienePermiso("053")) btnBuscar.visibility = View.GONE
        if (!tienePermiso("054")) btnEliminar.visibility = View.GONE

        cargarMedios(spinnerMedios)

        btnIrACrear.setOnClickListener {
            val intent = Intent(this, InsertarMedioTransporteActivity::class.java)
            startActivity(intent)
        }

        btnBuscar.setOnClickListener {
            val pos = spinnerMedios.selectedItemPosition
            if (pos != -1) {
                medioActual = medios[pos]
                editTipo.setText(medioActual?.tipo)
                editCapacidad.setText(medioActual?.capacidadMax.toString())
                Toast.makeText(this, getString(R.string.record_found), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.search_id_required), Toast.LENGTH_SHORT).show()
            }
        }

        btnActualizar.setOnClickListener {
            if (medioActual != null) {
                val nuevoTipo = editTipo.text.toString()
                val nuevaCapacidad = editCapacidad.text.toString().toIntOrNull()

                if (nuevoTipo.isNotEmpty() && nuevaCapacidad != null) {
                    val medioEditado = MedioTransporte(
                        idMedio = medioActual?.idMedio,
                        tipo = nuevoTipo,
                        capacidadMax = nuevaCapacidad
                    )
                    val filasAfectadas = handler.actualizar(medioEditado)
                    if (filasAfectadas > 0) {
                        Toast.makeText(this, getString(R.string.update_success), Toast.LENGTH_SHORT).show()
                        cargarMedios(spinnerMedios)
                    } else {
                        Toast.makeText(this, getString(R.string.update_error), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.search_required), Toast.LENGTH_SHORT).show()
            }
        }

        btnEliminar.setOnClickListener {
            if (medioActual != null) {
                val id = medioActual?.idMedio ?: -1
                val filasEliminadas = handler.eliminar(id)
                if (filasEliminadas > 0) {
                    Toast.makeText(this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show()
                    limpiarCampos(editTipo, editCapacidad)
                    medioActual = null
                    cargarMedios(spinnerMedios)
                } else {
                    Toast.makeText(this, getString(R.string.delete_error), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.search_required), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun limpiarCampos(tipo: EditText, capacidad: EditText) {
        tipo.text.clear()
        capacidad.text.clear()
    }

    private fun cargarMedios(spinner: Spinner) {
        medios = handler.obtenerTodos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, medios.map { "${it.idMedio} - ${it.tipo}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
