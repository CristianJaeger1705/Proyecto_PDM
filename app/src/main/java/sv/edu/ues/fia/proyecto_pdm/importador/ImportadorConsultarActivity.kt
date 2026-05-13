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
                Toast.makeText(this, "Ingrese un NUI para buscar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val importador = handler.buscar(nui)
            if (importador != null) {
                textResultado.text = """
                    NUI: ${importador.nui}
                    Nombre: ${importador.nombre} ${importador.apellido}
                    Apellido Casada: ${importador.apellidoCasada ?: "N/A"}
                    Género: ${importador.genero}
                    Fecha Nacimiento: ${importador.fechaNacimiento}
                    Dirección: ${importador.direccion}
                    Correo: ${importador.correoElectronico}
                    Responsable: ${importador.nuiResponsable ?: "N/A"}
                """.trimIndent()
            } else {
                textResultado.text = "No se encontró importador con NUI: $nui"
            }
        }

        btnLimpiar.setOnClickListener {
            editNUI.setText("")
            textResultado.text = "Resultado..."
        }
    }
}