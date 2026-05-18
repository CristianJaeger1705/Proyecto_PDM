package sv.edu.ues.fia.proyecto_pdm.taller

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R

class TallerInsertarActivity : AppCompatActivity() {

    private lateinit var handler: TallerHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taller_insertar)

        handler = TallerHandler(this)

        // val editId = findViewById<EditText>(R.id.editId) // Eliminado
        val editNombre = findViewById<EditText>(R.id.editNombre)
        val editDireccion = findViewById<EditText>(R.id.editDireccion)
        val editTelefono = findViewById<EditText>(R.id.editTelefono)
        val checkAutorizado = findViewById<CheckBox>(R.id.checkAutorizado)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)

        btnGuardar.setOnClickListener {
            // val idStr = editId.text.toString() // Eliminado
            val nombre = editNombre.text.toString()
            val direccion = editDireccion.text.toString()
            val telefono = editTelefono.text.toString()
            val autorizado = if (checkAutorizado.isChecked) "S" else "N"

            if (nombre.isNotEmpty()) {
                val taller = Taller(
                    idTaller = 0, // El ID será generado por la DB
                    nombreTaller = nombre,
                    direccion = direccion,
                    telefono = telefono,
                    autorizado = autorizado,
                )
                
                val resultado = handler.insertar(taller)
                if (resultado != -1L) {
                    Toast.makeText(this, getString(R.string.msg_workshop_saved, resultado), Toast.LENGTH_SHORT).show()
                    limpiar()
                } else {
                    Toast.makeText(this, getString(R.string.insert_error), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.msg_workshop_name_required), Toast.LENGTH_SHORT).show()
            }
        }

        btnLimpiar.setOnClickListener {
            limpiar()
        }
    }

    private fun limpiar() {
        // findViewById<EditText>(R.id.editId).text.clear() // Eliminado
        findViewById<EditText>(R.id.editNombre).text.clear()
        findViewById<EditText>(R.id.editDireccion).text.clear()
        findViewById<EditText>(R.id.editTelefono).text.clear()
        findViewById<CheckBox>(R.id.checkAutorizado).isChecked = false
    }
}
