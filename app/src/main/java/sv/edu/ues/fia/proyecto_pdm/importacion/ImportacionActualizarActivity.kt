package sv.edu.ues.fia.proyecto_pdm.importacion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R

class ImportacionActualizarActivity : AppCompatActivity() {

    private lateinit var editIdBuscar: EditText
    private lateinit var editIdImportador: EditText
    private lateinit var editCantidad: EditText
    private lateinit var editFecha: EditText
    private lateinit var btnCargar: Button
    private lateinit var btnActualizar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var handler: ImportacionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_importacion_actualizar)

        handler = ImportacionHandler(this)

        editIdBuscar = findViewById(R.id.editActualizarIdImportacion)
        editIdImportador = findViewById(R.id.editActualizarIdImportador)
        editCantidad = findViewById(R.id.editActualizarCantidadVehiculos)
        editFecha = findViewById(R.id.editActualizarFecha)
        btnCargar = findViewById(R.id.btnCargarActualizar)
        btnActualizar = findViewById(R.id.btnActualizarImportacion)
        btnLimpiar = findViewById(R.id.btnLimpiarActualizar)

        btnCargar.setOnClickListener {
            cargarDatos()
        }

        btnActualizar.setOnClickListener {
            actualizarImportacion()
        }

        btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
    }

    private fun cargarDatos() {
        val idStr = editIdBuscar.text.toString()
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID para buscar", Toast.LENGTH_SHORT).show()
            return
        }

        val importacion = handler.consultar(idStr.toInt())

        if (importacion != null) {
            editIdImportador.setText(importacion.idImportador)
            editCantidad.setText(importacion.cantidadVehiculos.toString())
            editFecha.setText(importacion.fecha)

            // Habilitar campos y botón de guardar
            editIdImportador.isEnabled = true
            editCantidad.isEnabled = true
            editFecha.isEnabled = true
            btnActualizar.isEnabled = true
            editIdBuscar.isEnabled = false
        } else {
            Toast.makeText(this, "Importación no encontrada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarImportacion() {
        val idStr = editIdBuscar.text.toString()
        val idImportador = editIdImportador.text.toString()
        val cantidadStr = editCantidad.text.toString()
        val fecha = editFecha.text.toString()

        if (idImportador.isEmpty() || cantidadStr.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val importacion = Importacion(
            idImportacion = idStr.toInt(),
            idImportador = idImportador,
            cantidadVehiculos = cantidadStr.toInt(),
            fecha = fecha
        )

        val filasAfectadas = handler.actualizar(importacion)

        if (filasAfectadas > 0) {
            Toast.makeText(this, "Importación actualizada con éxito", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        editIdBuscar.setText("")
        editIdImportador.setText("")
        editCantidad.setText("")
        editFecha.setText("")
        
        editIdImportador.isEnabled = false
        editCantidad.isEnabled = false
        editFecha.isEnabled = false
        btnActualizar.isEnabled = false
        editIdBuscar.isEnabled = true
    }
}
