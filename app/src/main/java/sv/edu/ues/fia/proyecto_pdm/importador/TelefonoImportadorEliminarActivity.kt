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
import sv.edu.ues.fia.proyecto_pdm.R

class TelefonoImportadorEliminarActivity : AppCompatActivity() {

    private lateinit var handler: TelefonoImportadorHandler
    private lateinit var listaTelefonos: List<TelefonoImportador>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_telefono_importador_eliminar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handler = TelefonoImportadorHandler(this)

        val spinnerId = findViewById<Spinner>(R.id.spinnerEliminarTelefonoId)
        val btnEliminar = findViewById<Button>(R.id.btnEliminarTelefono)

        cargarTelefonos(spinnerId)

        btnEliminar.setOnClickListener {
            val pos = spinnerId.selectedItemPosition
            if (pos == -1) return@setOnClickListener

            val telefono = listaTelefonos[pos]
            val id = telefono.idTelefono!!

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_confirm_delete))
                .setMessage(getString(R.string.msg_confirm_delete_phone, telefono.numero))
                .setPositiveButton(getString(R.string.btn_delete), { _, _ ->
                    val resultado = handler.eliminar(id)
                    if (resultado > 0) {
                        Toast.makeText(this, getString(R.string.msg_deleted_success), Toast.LENGTH_SHORT).show()
                        cargarTelefonos(spinnerId)
                    } else {
                        Toast.makeText(this, getString(R.string.msg_deleted_error), Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show()
        }
    }

    private fun cargarTelefonos(spinner: Spinner) {
        listaTelefonos = handler.obtenerTodos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaTelefonos.map { "ID:${it.idTelefono} - ${it.nui} (${it.numero})" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}