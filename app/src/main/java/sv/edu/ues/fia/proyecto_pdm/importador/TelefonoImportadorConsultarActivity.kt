package sv.edu.ues.fia.proyecto_pdm.importador

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sv.edu.ues.fia.proyecto_pdm.R

class TelefonoImportadorConsultarActivity : AppCompatActivity() {

    private lateinit var handler: TelefonoImportadorHandler
    private lateinit var importadorHandler: sv.edu.ues.fia.proyecto_pdm.ImportadorHandler
    private lateinit var listaImportadores: List<sv.edu.ues.fia.proyecto_pdm.Importador>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_telefono_importador_consultar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handler = TelefonoImportadorHandler(this)
        importadorHandler = sv.edu.ues.fia.proyecto_pdm.ImportadorHandler(this)

        val spinnerNUI = findViewById<Spinner>(R.id.spinnerConsultarTelefonoNUI)
        val btnConsultar = findViewById<Button>(R.id.btnConsultarTelefono)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiarConsultarTelefono)
        val textResultado = findViewById<TextView>(R.id.textResultadoTelefono)

        cargarImportadores(spinnerNUI)

        btnConsultar.setOnClickListener {
            val pos = spinnerNUI.selectedItemPosition
            if (pos == -1) return@setOnClickListener

            val nui = listaImportadores[pos].nui
            val telefonos = handler.obtenerPorImportador(nui)
            if (telefonos.isNotEmpty()) {
                val sb = StringBuilder()
                telefonos.forEach { t ->
                    sb.append(getString(R.string.label_result_phone_id, t.idTelefono)).append("\n")
                    sb.append(getString(R.string.label_result_phone_number, t.numero)).append("\n")
                    sb.append(getString(R.string.label_result_phone_type, t.tipoTelefono ?: "N/A")).append("\n")
                    sb.append("---\n")
                }
                textResultado.text = sb.toString()
            } else {
                textResultado.text = getString(R.string.msg_no_phones_found, nui)
            }
        }

        btnLimpiar.setOnClickListener {
            textResultado.text = getString(R.string.label_result)
        }
    }

    private fun cargarImportadores(spinner: Spinner) {
        listaImportadores = importadorHandler.obtenerTodos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaImportadores.map { "${it.nui} - ${it.nombre}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}