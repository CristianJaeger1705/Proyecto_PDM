package sv.edu.ues.fia.proyecto_pdm.importacion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R

class ImportacionConsultarActivity : AppCompatActivity() {

    private lateinit var editIdConsultar: EditText
    private lateinit var editIdImportador: EditText
    private lateinit var editCantidad: EditText
    private lateinit var editFecha: EditText
    private lateinit var btnConsultar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var handler: ImportacionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_importacion_consultar)

        handler = ImportacionHandler(this)

        editIdConsultar = findViewById(R.id.editConsultarIdImportacion)
        editIdImportador = findViewById(R.id.editConsultarIdImportador)
        editCantidad = findViewById(R.id.editConsultarCantidadVehiculos)
        editFecha = findViewById(R.id.editConsultarFecha)
        btnConsultar = findViewById(R.id.btnConsultarImportacion)
        btnLimpiar = findViewById(R.id.btnLimpiarConsultar)

        btnConsultar.setOnClickListener {
            consultarImportacion()
        }

        btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
    }

    private fun consultarImportacion() {
        val idStr = editIdConsultar.text.toString()
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID de importación", Toast.LENGTH_SHORT).show()
            return
        }

        val importacion = handler.consultar(idStr.toInt())

        if (importacion != null) {
            editIdImportador.setText(importacion.idImportador)
            editCantidad.setText(importacion.cantidadVehiculos.toString())
            editFecha.setText(importacion.fecha)
        } else {
            Toast.makeText(this, "Importación no encontrada", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        }
    }

    private fun limpiarCampos() {
        editIdConsultar.setText("")
        editIdImportador.setText("")
        editCantidad.setText("")
        editFecha.setText("")
    }
}
