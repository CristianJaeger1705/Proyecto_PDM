package sv.edu.ues.fia.proyecto_pdm.importacion

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R

class ImportacionEliminarActivity : AppCompatActivity() {

    private lateinit var spinnerIdEliminar: Spinner
    private lateinit var btnEliminar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var handler: ImportacionHandler
    private lateinit var listaImportaciones: List<Importacion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_importacion_eliminar)

        handler = ImportacionHandler(this)

        spinnerIdEliminar = findViewById(R.id.spinnerEliminarIdImportacion)
        btnEliminar = findViewById(R.id.btnEliminarImportacion)
        btnLimpiar = findViewById(R.id.btnLimpiarEliminar)

        cargarImportaciones()

        btnEliminar.setOnClickListener {
            eliminarImportacion()
        }

        btnLimpiar.setOnClickListener {
            // No hay mucho que limpiar con un spinner
        }
    }

    private fun cargarImportaciones() {
        listaImportaciones = handler.obtenerTodas()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaImportaciones.map { "ID: ${it.idImportacion} - ${it.idImportador}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerIdEliminar.adapter = adapter
    }

    private fun eliminarImportacion() {
        val pos = spinnerIdEliminar.selectedItemPosition
        if (pos != -1) {
            val id = listaImportaciones[pos].idImportacion
            val resultado = handler.eliminar(id!!) // id is Int? in the data class, but should be present here

            if (resultado > 0) {
                Toast.makeText(this, getString(R.string.msg_importacion_deleted), Toast.LENGTH_SHORT).show()
                cargarImportaciones()
            } else {
                Toast.makeText(this, getString(R.string.msg_importacion_error_delete), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
