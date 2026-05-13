package sv.edu.ues.fia.proyecto_pdm.transporte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R

class GestionTransporteActivity : AppCompatActivity() {

    private lateinit var handler: MedioTransporteHandler
    private var medioActual: MedioTransporte? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_transporte)

        handler = MedioTransporteHandler(this)

        val editBusquedaId = findViewById<EditText>(R.id.editBusquedaId)
        val editTipo = findViewById<EditText>(R.id.editTipo)
        val editCapacidad = findViewById<EditText>(R.id.editCapacidad)

        val btnIrACrear = findViewById<Button>(R.id.btnIrACrear)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)

        btnIrACrear.setOnClickListener {
            val intent = Intent(this, InsertarMedioTransporteActivity::class.java)
            startActivity(intent)
        }

        btnBuscar.setOnClickListener {
            val idStr = editBusquedaId.text.toString()
            if (idStr.isNotEmpty()) {
                val id = idStr.toInt()
                medioActual = handler.buscar(id)
                
                if (medioActual != null) {
                    editTipo.setText(medioActual?.tipo)
                    editCapacidad.setText(medioActual?.capacidadMax.toString())
                    Toast.makeText(this, "Registro encontrado", Toast.LENGTH_SHORT).show()
                } else {
                    limpiarCampos(editTipo, editCapacidad)
                    Toast.makeText(this, "No se encontró el ID: $id", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Ingrese un ID para buscar", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Primero busque un registro", Toast.LENGTH_SHORT).show()
            }
        }

        btnEliminar.setOnClickListener {
            if (medioActual != null) {
                val id = medioActual?.idMedio ?: -1
                val filasEliminadas = handler.eliminar(id)
                if (filasEliminadas > 0) {
                    Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show()
                    limpiarCampos(editTipo, editCapacidad)
                    editBusquedaId.text.clear()
                    medioActual = null
                } else {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Primero busque un registro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun limpiarCampos(tipo: EditText, capacidad: EditText) {
        tipo.text.clear()
        capacidad.text.clear()
    }
}
