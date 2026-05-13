package sv.edu.ues.fia.proyecto_pdm.importador

import android.app.DatePickerDialog
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
import sv.edu.ues.fia.proyecto_pdm.Importador
import sv.edu.ues.fia.proyecto_pdm.ImportadorHandler
import sv.edu.ues.fia.proyecto_pdm.R
import java.util.Calendar

class ImportadorActualizarActivity : AppCompatActivity() {

    private lateinit var handler: ImportadorHandler
    private var importadorActual: Importador? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_importador_actualizar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handler = ImportadorHandler(this)

        val editNUI = findViewById<EditText>(R.id.editActualizarNUI)
        val editNombre = findViewById<EditText>(R.id.editActualizarNombre)
        val editApellido = findViewById<EditText>(R.id.editActualizarApellido)
        val editApellidoCasada = findViewById<EditText>(R.id.editActualizarApellidoCasada)
        val spinnerGenero = findViewById<Spinner>(R.id.spinnerActualizarGenero)
        val editFecha = findViewById<EditText>(R.id.editActualizarFecha)
        val editDireccion = findViewById<EditText>(R.id.editActualizarDireccion)
        val editCorreo = findViewById<EditText>(R.id.editActualizarCorreo)
        val editNUIResponsable = findViewById<EditText>(R.id.editActualizarNUIResponsable)
        val btnCargar = findViewById<Button>(R.id.btnCargarImportador)
        val btnActualizar = findViewById<Button>(R.id.btnActualizarImportador)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiarActualizarImportador)

        val generos = arrayOf("M", "F")
        val adapterGenero = ArrayAdapter(this, android.R.layout.simple_spinner_item, generos)
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenero.adapter = adapterGenero

        editFecha.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                editFecha.setText("$year-${String.format("%02d", month + 1)}-${String.format("%02d", day)}")
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnCargar.setOnClickListener {
            val nui = editNUI.text.toString().trim()
            if (nui.isEmpty()) {
                Toast.makeText(this, "Ingrese un NUI para cargar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            importadorActual = handler.buscar(nui)
            if (importadorActual != null) {
                editNombre.setText(importadorActual!!.nombre)
                editApellido.setText(importadorActual!!.apellido)
                editApellidoCasada.setText(importadorActual!!.apellidoCasada ?: "")
                spinnerGenero.setSelection(if (importadorActual!!.genero == "M") 0 else 1)
                editFecha.setText(importadorActual!!.fechaNacimiento)
                editDireccion.setText(importadorActual!!.direccion)
                editCorreo.setText(importadorActual!!.correoElectronico)
                editNUIResponsable.setText(importadorActual!!.nuiResponsable ?: "")
                setEdicionHabilitada(true)
                Toast.makeText(this, "Datos cargados", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se encontró importador con ese NUI", Toast.LENGTH_SHORT).show()
                setEdicionHabilitada(false)
            }
        }

        btnActualizar.setOnClickListener {
            if (importadorActual == null) return@setOnClickListener
            val apellidoCasada = editApellidoCasada.text.toString().trim().ifEmpty { null }
            val nuiResponsable = editNUIResponsable.text.toString().trim().ifEmpty { null }

            val actualizado = Importador(
                importadorActual!!.nui,
                editNombre.text.toString().trim(),
                editApellido.text.toString().trim(),
                apellidoCasada,
                spinnerGenero.selectedItem.toString(),
                editFecha.text.toString().trim(),
                editDireccion.text.toString().trim(),
                editCorreo.text.toString().trim(),
                nuiResponsable
            )
            val filas = handler.actualizar(actualizado)
            if (filas > 0) {
                Toast.makeText(this, "Importador actualizado con éxito", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }

        btnLimpiar.setOnClickListener { limpiarCampos() }
        setEdicionHabilitada(false)
    }

    private fun setEdicionHabilitada(habilitar: Boolean) {
        findViewById<EditText>(R.id.editActualizarNombre).isEnabled = habilitar
        findViewById<EditText>(R.id.editActualizarApellido).isEnabled = habilitar
        findViewById<EditText>(R.id.editActualizarApellidoCasada).isEnabled = habilitar
        findViewById<Spinner>(R.id.spinnerActualizarGenero).isEnabled = habilitar
        findViewById<EditText>(R.id.editActualizarFecha).isEnabled = habilitar
        findViewById<EditText>(R.id.editActualizarDireccion).isEnabled = habilitar
        findViewById<EditText>(R.id.editActualizarCorreo).isEnabled = habilitar
        findViewById<EditText>(R.id.editActualizarNUIResponsable).isEnabled = habilitar
        findViewById<Button>(R.id.btnActualizarImportador).isEnabled = habilitar
        findViewById<EditText>(R.id.editActualizarNUI).isEnabled = !habilitar
    }

    private fun limpiarCampos() {
        importadorActual = null
        findViewById<EditText>(R.id.editActualizarNUI).setText("")
        findViewById<EditText>(R.id.editActualizarNombre).setText("")
        findViewById<EditText>(R.id.editActualizarApellido).setText("")
        findViewById<EditText>(R.id.editActualizarApellidoCasada).setText("")
        findViewById<EditText>(R.id.editActualizarFecha).setText("")
        findViewById<EditText>(R.id.editActualizarDireccion).setText("")
        findViewById<EditText>(R.id.editActualizarCorreo).setText("")
        findViewById<EditText>(R.id.editActualizarNUIResponsable).setText("")
        setEdicionHabilitada(false)
        findViewById<EditText>(R.id.editActualizarNUI).requestFocus()
    }
}