package sv.edu.ues.fia.proyecto_pdm.importacion

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.Importador
import sv.edu.ues.fia.proyecto_pdm.ImportadorHandler
import sv.edu.ues.fia.proyecto_pdm.R
import java.util.Calendar
import java.util.Locale

class ImportacionInsertarActivity : AppCompatActivity() {

    private lateinit var editIdImportacion: EditText
    private lateinit var spinnerImportador: Spinner
    private lateinit var editCantidadVehiculos: EditText
    private lateinit var editFechaImportacion: EditText
    private lateinit var btnInsertar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var handler: ImportacionHandler
    private lateinit var importadorHandler: ImportadorHandler
    private lateinit var listaImportadores: List<Importador>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_importacion_insertar)

        handler = ImportacionHandler(this)
        importadorHandler = ImportadorHandler(this)

        editIdImportacion = findViewById(R.id.editIdImportacion)
        spinnerImportador = findViewById(R.id.spinnerInsertNUI)
        editCantidadVehiculos = findViewById(R.id.editCantidadVehiculos)
        editFechaImportacion = findViewById(R.id.editFechaImportacion)
        btnInsertar = findViewById(R.id.btnInsertarImportacion)
        btnLimpiar = findViewById(R.id.btnLimpiarImportacion)

        cargarImportadores()

        editFechaImportacion.setOnClickListener {
            mostrarDatePicker()
        }

        btnInsertar.setOnClickListener {
            insertarImportacion()
        }

        btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
    }

    private fun cargarImportadores() {
        listaImportadores = importadorHandler.obtenerTodos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaImportadores.map { "${it.nui} - ${it.nombre}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerImportador.adapter = adapter
    }

    private fun mostrarDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, y, m, d ->
            val formattedDate = String.format(Locale.US, "%04d-%02d-%02d", y, m + 1, d)
            editFechaImportacion.setText(formattedDate)
        }, year, month, day).show()
    }

    private fun insertarImportacion() {
        val idStr = editIdImportacion.text.toString()
        val posImp = spinnerImportador.selectedItemPosition
        val cantidadStr = editCantidadVehiculos.text.toString()
        val fecha = editFechaImportacion.text.toString()

        if (idStr.isEmpty() || posImp == -1 || cantidadStr.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val idImportador = listaImportadores[posImp].nui

        val importacion = Importacion(
            idImportacion = idStr.toInt(),
            idImportador = idImportador,
            cantidadVehiculos = cantidadStr.toInt(),
            fecha = fecha
        )

        val resultado = handler.insertar(importacion)

        if (resultado != -1L) {
            Toast.makeText(this, getString(R.string.msg_importacion_saved), Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, getString(R.string.msg_importacion_error_save), Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        editIdImportacion.setText("")
        editCantidadVehiculos.setText("")
        editFechaImportacion.setText("")
    }
}
