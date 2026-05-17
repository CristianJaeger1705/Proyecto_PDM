package sv.edu.ues.fia.proyecto_pdm.importador

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

        val editNUI = findViewById<EditText>(R.id.editConsultarNUI)
        val btnConsultar = findViewById<Button>(R.id.btnConsultarImportador)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiarConsultarImportador)
        val textResultado = findViewById<TextView>(R.id.textResultadoImportador)

        btnConsultar.setOnClickListener {
            val nui = editNUI.text.toString().trim()
            if (nui.isEmpty()) {
                Toast.makeText(this, getString(R.string.msg_enter_nui), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val importador = handler.buscar(nui)
            if (importador != null) {
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
            } else {
                textResultado.text = getString(R.string.msg_not_found_nui, nui)
            }
        }

        btnLimpiar.setOnClickListener {
            editNUI.setText("")
            textResultado.text = getString(R.string.label_result)
        }
    }
}