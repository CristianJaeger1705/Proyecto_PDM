package sv.edu.ues.fia.proyecto_pdm.importacion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R

class ImportacionEliminarActivity : AppCompatActivity() {

    private lateinit var editIdEliminar: EditText
    private lateinit var btnEliminar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var handler: ImportacionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_importacion_eliminar)

        handler = ImportacionHandler(this)

        editIdEliminar = findViewById(R.id.editEliminarIdImportacion)
        btnEliminar = findViewById(R.id.btnEliminarImportacion)
        btnLimpiar = findViewById(R.id.btnLimpiarEliminar)

        btnEliminar.setOnClickListener {
            eliminarImportacion()
        }

        btnLimpiar.setOnClickListener {
            editIdEliminar.setText("")
        }
    }

    private fun eliminarImportacion() {
        val idStr = editIdEliminar.text.toString()
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID de la importación", Toast.LENGTH_SHORT).show()
            return
        }

        val resultado = handler.eliminar(idStr.toInt())

        if (resultado > 0) {
            Toast.makeText(this, "Importación eliminada con éxito", Toast.LENGTH_SHORT).show()
            editIdEliminar.setText("")
        } else {
            Toast.makeText(this, "Error: No se encontró la importación o no pudo eliminarse", Toast.LENGTH_SHORT).show()
        }
    }
}
