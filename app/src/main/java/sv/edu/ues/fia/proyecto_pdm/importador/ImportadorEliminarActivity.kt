package sv.edu.ues.fia.proyecto_pdm.importador

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sv.edu.ues.fia.proyecto_pdm.ImportadorHandler
import sv.edu.ues.fia.proyecto_pdm.R

class ImportadorEliminarActivity : AppCompatActivity() {

    private lateinit var handler: ImportadorHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_importador_eliminar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handler = ImportadorHandler(this)

        val editNUI = findViewById<EditText>(R.id.editEliminarNUI)
        val btnEliminar = findViewById<Button>(R.id.btnEliminarImportador)

        btnEliminar.setOnClickListener {
            val nui = editNUI.text.toString().trim()
            if (nui.isEmpty()) {
                Toast.makeText(this, "Ingrese un NUI para eliminar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val importador = handler.buscar(nui)
            if (importador == null) {
                Toast.makeText(this, "No existe importador con NUI: $nui", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Eliminar a ${importador.nombre} ${importador.apellido}?")
                .setPositiveButton("Eliminar") { _, _ ->
                    val resultado = handler.eliminar(nui)
                    if (resultado > 0) {
                        Toast.makeText(this, "Importador eliminado", Toast.LENGTH_SHORT).show()
                        editNUI.setText("")
                    } else {
                        Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}