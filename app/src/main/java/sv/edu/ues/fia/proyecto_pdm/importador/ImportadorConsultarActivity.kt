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
import sv.edu.ues.fia.proyecto_pdm.ImportadorHandler
import sv.edu.ues.fia.proyecto_pdm.R

class ImportadorConsultarActivity : AppCompatActivity() {

    private lateinit var handler: ImportadorHandler
    private lateinit var listaImportadores: List<sv.edu.ues.fia.proyecto_pdm.Importador>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_importador_consultar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handler = ImportadorHandler(this)

        val spinnerNUI = findViewById<Spinner>(R.id.spinnerConsultarNUI)
        val btnConsultar = findViewById<Button>(R.id.btnConsultarImportador)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiarConsultarImportador)
        val textResultado = findViewById<TextView>(R.id.textResultadoImportador)

        cargarImportadores(spinnerNUI)

        btnConsultar.setOnClickListener {
            val pos = spinnerNUI.selectedItemPosition
            if (pos != -1) {
                val importador = listaImportadores[pos]
                val sb = StringBuilder()
                sb.append(getString(R.string.label_result_nui, importador.nui)).append("\n")
                sb.append(getString(R.string.label_result_name, importador.nombre, importador.apellido)).append("\n")
                sb.append(getString(R.string.label_result_maiden, importador.apellidoCasada ?: "N/A")).append("\n")
                sb.append(getString(R.string.label_result_gender, importador.genero)).append("\n")
                sb.append(getString(R.string.label_result_birth, importador.fechaNacimiento)).append("\n")
                sb.append(getString(R.string.label_result_address, importador.direccion)).append("\n")
                sb.append(getString(R.string.label_result_email, importador.correoElectronico)).append("\n")
                sb.append(getString(R.string.label_result_responsible, importador.nuiResponsable ?: "N/A"))
                textResultado.text = sb.toString()
            }
        }

        btnLimpiar.setOnClickListener {
            textResultado.text = getString(R.string.label_result)
        }
    }

    private fun cargarImportadores(spinner: Spinner) {
        listaImportadores = handler.obtenerTodos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaImportadores.map { "${it.nui} - ${it.nombre}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}