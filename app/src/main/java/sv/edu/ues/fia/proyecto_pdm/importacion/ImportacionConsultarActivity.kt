package sv.edu.ues.fia.proyecto_pdm.importacion

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R

class ImportacionConsultarActivity : AppCompatActivity() {

    private lateinit var spinnerIdConsultar: Spinner
    private lateinit var editIdImportador: EditText
    private lateinit var editCantidad: EditText
    private lateinit var editFecha: EditText
    private lateinit var btnConsultar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var handler: ImportacionHandler
    private lateinit var listaImportaciones: List<Importacion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_importacion_consultar)

        handler = ImportacionHandler(this)

        spinnerIdConsultar = findViewById(R.id.spinnerConsultarIdImportacion)
        editIdImportador = findViewById(R.id.editConsultarIdImportador)
        editCantidad = findViewById(R.id.editConsultarCantidadVehiculos)
        editFecha = findViewById(R.id.editConsultarFecha)
        btnConsultar = findViewById(R.id.btnConsultarImportacion)
        btnLimpiar = findViewById(R.id.btnLimpiarConsultar)

        cargarImportaciones()

        btnConsultar.setOnClickListener {
            consultarImportacion()
        }

        btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
    }

    private fun cargarImportaciones() {
        listaImportaciones = handler.obtenerTodas()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaImportaciones.map { "ID: ${it.idImportacion} - ${it.idImportador}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerIdConsultar.adapter = adapter
    }

    private fun consultarImportacion() {
        val pos = spinnerIdConsultar.selectedItemPosition
        if (pos != -1) {
            val importacion = listaImportaciones[pos]
            editIdImportador.setText(importacion.idImportador)
            editCantidad.setText(importacion.cantidadVehiculos.toString())
            editFecha.setText(importacion.fecha)
            Toast.makeText(this, getString(R.string.record_found), Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        editIdImportador.setText("")
        editCantidad.setText("")
        editFecha.setText("")
    }
}
