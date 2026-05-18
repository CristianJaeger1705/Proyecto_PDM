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
import sv.edu.ues.fia.proyecto_pdm.R

class TelefonoImportadorActualizarActivity : AppCompatActivity() {

    private lateinit var handler: TelefonoImportadorHandler
    private var telefonoActual: TelefonoImportador? = null
    private lateinit var listaTelefonos: List<TelefonoImportador>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_telefono_importador_actualizar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handler = TelefonoImportadorHandler(this)

        val spinnerId = findViewById<Spinner>(R.id.spinnerActualizarTelefonoId)
        val editNumero = findViewById<EditText>(R.id.editActualizarTelefonoNumero)
        val spinnerTipo = findViewById<Spinner>(R.id.spinnerActualizarTelefonoTipo)
        val btnCargar = findViewById<Button>(R.id.btnCargarTelefono)
        val btnActualizar = findViewById<Button>(R.id.btnActualizarTelefono)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiarActualizarTelefono)

        val tipos = arrayOf(
            getString(R.string.phone_type_mobile),
            getString(R.string.phone_type_home),
            getString(R.string.phone_type_work),
            getString(R.string.phone_type_other)
        )
        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos)
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipo.adapter = adapterTipo

        cargarTelefonos(spinnerId)

        btnCargar.setOnClickListener {
            val pos = spinnerId.selectedItemPosition
            if (pos != -1) {
                telefonoActual = listaTelefonos[pos]
                editNumero.setText(telefonoActual!!.numero)
                val index = tipos.indexOf(telefonoActual!!.tipoTelefono)
                if (index != -1) spinnerTipo.setSelection(index)
                setEdicionHabilitada(true)
                Toast.makeText(this, getString(R.string.record_found), Toast.LENGTH_SHORT).show()
            }
        }

        btnActualizar.setOnClickListener {
            if (telefonoActual == null) return@setOnClickListener
            val actualizado = TelefonoImportador(
                telefonoActual!!.idTelefono,
                telefonoActual!!.nui,
                editNumero.text.toString().trim(),
                spinnerTipo.selectedItem.toString()
            )
            val filas = handler.actualizar(actualizado)
            if (filas > 0) {
                Toast.makeText(this, getString(R.string.update_success), Toast.LENGTH_SHORT).show()
                limpiarCampos()
                cargarTelefonos(spinnerId)
            } else {
                Toast.makeText(this, getString(R.string.update_error), Toast.LENGTH_SHORT).show()
            }
        }

        btnLimpiar.setOnClickListener { limpiarCampos() }
        setEdicionHabilitada(false)
    }

    private fun cargarTelefonos(spinner: Spinner) {
        listaTelefonos = handler.obtenerTodos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaTelefonos.map { "ID:${it.idTelefono} - ${it.nui} (${it.numero})" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setEdicionHabilitada(habilitar: Boolean) {
        findViewById<EditText>(R.id.editActualizarTelefonoNumero).isEnabled = habilitar
        findViewById<Spinner>(R.id.spinnerActualizarTelefonoTipo).isEnabled = habilitar
        findViewById<Button>(R.id.btnActualizarTelefono).isEnabled = habilitar
        findViewById<Spinner>(R.id.spinnerActualizarTelefonoId).isEnabled = !habilitar
    }

    private fun limpiarCampos() {
        telefonoActual = null
        findViewById<EditText>(R.id.editActualizarTelefonoNumero).setText("")
        setEdicionHabilitada(false)
    }
}