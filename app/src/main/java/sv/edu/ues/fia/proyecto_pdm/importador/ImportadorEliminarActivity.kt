package sv.edu.ues.fia.proyecto_pdm.importador

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
    private lateinit var listaImportadores: List<sv.edu.ues.fia.proyecto_pdm.Importador>

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

        val spinnerNUI = findViewById<Spinner>(R.id.spinnerEliminarNUI)
        val btnEliminar = findViewById<Button>(R.id.btnEliminarImportador)

        cargarImportadores(spinnerNUI)

        btnEliminar.setOnClickListener {
            val pos = spinnerNUI.selectedItemPosition
            if (pos == -1) return@setOnClickListener

            val importador = listaImportadores[pos]
            val nui = importador.nui

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_confirm_delete))
                .setMessage(getString(R.string.msg_confirm_delete_importador, importador.nombre, importador.apellido))
                .setPositiveButton(getString(R.string.btn_delete)) { _, _ ->
                    val resultado = handler.eliminar(nui)
                    if (resultado > 0) {
                        Toast.makeText(this, getString(R.string.msg_deleted_success), Toast.LENGTH_SHORT).show()
                        cargarImportadores(spinnerNUI)
                    } else {
                        Toast.makeText(this, getString(R.string.msg_deleted_error), Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show()
        }
    }

    private fun cargarImportadores(spinner: Spinner) {
        listaImportadores = handler.obtenerTodos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaImportadores.map { "${it.nui} - ${it.nombre}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}