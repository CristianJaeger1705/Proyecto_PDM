package sv.edu.ues.fia.proyecto_pdm.importador

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sv.edu.ues.fia.proyecto_pdm.ImportadorHandler
import sv.edu.ues.fia.proyecto_pdm.R

class TelefonoImportadorInsertarActivity : AppCompatActivity() {

    private lateinit var handler: TelefonoImportadorHandler
    private lateinit var importadorHandler: ImportadorHandler
    private lateinit var listaImportadores: List<sv.edu.ues.fia.proyecto_pdm.Importador>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_telefono_importador_insertar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handler = TelefonoImportadorHandler(this)
        importadorHandler = ImportadorHandler(this)

        val spinnerNUI = findViewById<Spinner>(R.id.spinnerTelefonoNUI)
        val editNumero = findViewById<EditText>(R.id.editTelefonoNumero)
        val spinnerTipo = findViewById<Spinner>(R.id.spinnerTelefonoTipo)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarTelefono)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiarTelefono)

        // Cargar Importadores
        listaImportadores = importadorHandler.obtenerTodos()
        val adapterImp = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaImportadores.map { "${it.nui} - ${it.nombre}" })
        adapterImp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerNUI.adapter = adapterImp

        val tipos = arrayOf(
            getString(R.string.phone_type_mobile),
            getString(R.string.phone_type_home),
            getString(R.string.phone_type_work),
            getString(R.string.phone_type_other)
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipo.adapter = adapter

        btnGuardar.setOnClickListener {
            val posImp = spinnerNUI.selectedItemPosition
            val numero = editNumero.text.toString().trim()
            val tipo = spinnerTipo.selectedItem.toString()

            if (posImp == -1 || numero.isEmpty()) {
                Toast.makeText(this, getString(R.string.required_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nui = listaImportadores[posImp].nui
            val telefono = TelefonoImportador(null, nui, numero, tipo)
            val resultado = handler.insertar(telefono)

            if (resultado != -1L) {
                Toast.makeText(this, getString(R.string.msg_phone_saved, resultado), Toast.LENGTH_SHORT).show()
                limpiarCampos()
            } else {
                Toast.makeText(this, getString(R.string.insert_error), Toast.LENGTH_SHORT).show()
            }
        }

        btnLimpiar.setOnClickListener { limpiarCampos() }
    }

    private fun limpiarCampos() {
        findViewById<EditText>(R.id.editTelefonoNumero).setText("")
    }
}