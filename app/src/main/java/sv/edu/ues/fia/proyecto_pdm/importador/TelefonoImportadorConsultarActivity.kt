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
import sv.edu.ues.fia.proyecto_pdm.R

class TelefonoImportadorConsultarActivity : AppCompatActivity() {

    private lateinit var handler: TelefonoImportadorHandler

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

        val editNUI = findViewById<EditText>(R.id.editConsultarTelefonoNUI)
        val btnConsultar = findViewById<Button>(R.id.btnConsultarTelefono)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiarConsultarTelefono)
        val textResultado = findViewById<TextView>(R.id.textResultadoTelefono)

        btnConsultar.setOnClickListener {
            val nui = editNUI.text.toString().trim()
            if (nui.isEmpty()) {
                Toast.makeText(this, getString(R.string.msg_enter_nui), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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
            editNUI.setText("")
            textResultado.text = getString(R.string.label_result)
        }
    }
}