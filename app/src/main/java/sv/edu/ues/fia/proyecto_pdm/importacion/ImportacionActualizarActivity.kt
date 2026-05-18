package sv.edu.ues.fia.proyecto_pdm.importacion

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R
import java.util.Calendar
import java.util.Locale

class ImportacionActualizarActivity : AppCompatActivity() {

    private lateinit var spinnerIdBuscar: Spinner
    private lateinit var editIdImportador: EditText
    private lateinit var editCantidad: EditText
    private lateinit var editFecha: EditText
    private lateinit var btnCargar: Button
    private lateinit var btnActualizar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var handler: ImportacionHandler
    private lateinit var listaImportaciones: List<Importacion>
    private var importacionActual: Importacion? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_importacion_actualizar)

        handler = ImportacionHandler(this)

        spinnerIdBuscar = findViewById(R.id.spinnerActualizarIdImportacion)
        editIdImportador = findViewById(R.id.editActualizarIdImportador)
        editCantidad = findViewById(R.id.editActualizarCantidadVehiculos)
        editFecha = findViewById(R.id.editActualizarFecha)
        btnCargar = findViewById(R.id.btnCargarActualizar)
        btnActualizar = findViewById(R.id.btnActualizarImportacion)
        btnLimpiar = findViewById(R.id.btnLimpiarActualizar)

        cargarImportaciones()

        editFecha.setOnClickListener {
            mostrarDatePicker()
        }

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

    private fun cargarImportaciones() {
        listaImportaciones = handler.obtenerTodas()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaImportaciones.map { "ID: ${it.idImportacion} - ${it.idImportador}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerIdBuscar.adapter = adapter
    }

    private fun mostrarDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, y, m, d ->
            val formattedDate = String.format(Locale.US, "%04d-%02d-%02d", y, m + 1, d)
            editFecha.setText(formattedDate)
        }, year, month, day).show()
    }

    private fun cargarDatos() {
        val pos = spinnerIdBuscar.selectedItemPosition
        if (pos != -1) {
            importacionActual = listaImportaciones[pos]
            editIdImportador.setText(importacionActual!!.idImportador)
            editCantidad.setText(importacionActual!!.cantidadVehiculos.toString())
            editFecha.setText(importacionActual!!.fecha)

            // Habilitar campos y botón de guardar
            editIdImportador.isEnabled = true
            editCantidad.isEnabled = true
            editFecha.isEnabled = true
            btnActualizar.isEnabled = true
            spinnerIdBuscar.isEnabled = false
            Toast.makeText(this, getString(R.string.record_found), Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarImportacion() {
        if (importacionActual == null) return

        val idImportador = editIdImportador.text.toString()
        val cantidadStr = editCantidad.text.toString()
        val fecha = editFecha.text.toString()

        if (idImportador.isEmpty() || cantidadStr.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val importacion = Importacion(
            idImportacion = importacionActual!!.idImportacion,
            idImportador = idImportador,
            cantidadVehiculos = cantidadStr.toInt(),
            fecha = fecha
        )

        val filasAfectadas = handler.actualizar(importacion)

        if (filasAfectadas > 0) {
            Toast.makeText(this, getString(R.string.msg_importacion_updated), Toast.LENGTH_SHORT).show()
            limpiarCampos()
            cargarImportaciones()
        } else {
            Toast.makeText(this, getString(R.string.update_error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        importacionActual = null
        editIdImportador.setText("")
        editCantidad.setText("")
        editFecha.setText("")
        
        editIdImportador.isEnabled = false
        editCantidad.isEnabled = false
        editFecha.isEnabled = false
        btnActualizar.isEnabled = false
        spinnerIdBuscar.isEnabled = true
    }
}
