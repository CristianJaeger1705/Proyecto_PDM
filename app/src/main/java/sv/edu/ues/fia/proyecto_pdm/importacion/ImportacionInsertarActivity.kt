package sv.edu.ues.fia.proyecto_pdm.importacion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.ImportadorHandler
import sv.edu.ues.fia.proyecto_pdm.R

class ImportacionInsertarActivity : AppCompatActivity() {

    private lateinit var editIdImportacion: EditText
    private lateinit var editIdImportador: EditText
    private lateinit var editCantidadVehiculos: EditText
    private lateinit var editFechaImportacion: EditText
    private lateinit var btnInsertar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var handler: ImportacionHandler
    private lateinit var importadorHandler: ImportadorHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_importacion_insertar)

        handler = ImportacionHandler(this)
        importadorHandler = ImportadorHandler(this)

        editIdImportacion = findViewById(R.id.editIdImportacion)
        editIdImportador = findViewById(R.id.editIdImportador)
        editCantidadVehiculos = findViewById(R.id.editCantidadVehiculos)
        editFechaImportacion = findViewById(R.id.editFechaImportacion)
        btnInsertar = findViewById(R.id.btnInsertarImportacion)
        btnLimpiar = findViewById(R.id.btnLimpiarImportacion)

        btnInsertar.setOnClickListener {
            insertarImportacion()
        }

        btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
    }

    private fun insertarImportacion() {
        val idStr = editIdImportacion.text.toString()
        val idImportador = editIdImportador.text.toString()
        val cantidadStr = editCantidadVehiculos.text.toString()
        val fecha = editFechaImportacion.text.toString()

        if (idStr.isEmpty() || idImportador.isEmpty() || cantidadStr.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Verificar si el importador existe
        val importador = importadorHandler.buscar(idImportador)
        if (importador == null) {
            Toast.makeText(this, "El importador con NUI $idImportador no existe", Toast.LENGTH_LONG).show()
            return
        }

        val importacion = Importacion(
            idImportacion = idStr.toInt(),
            idImportador = idImportador,
            cantidadVehiculos = cantidadStr.toInt(),
            fecha = fecha
        )

        val resultado = handler.insertar(importacion)

        if (resultado != -1L) {
            Toast.makeText(this, "Importación insertada con éxito", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Error al insertar importación", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        editIdImportacion.setText("")
        editIdImportador.setText("")
        editCantidadVehiculos.setText("")
        editFechaImportacion.setText("")
    }
}
