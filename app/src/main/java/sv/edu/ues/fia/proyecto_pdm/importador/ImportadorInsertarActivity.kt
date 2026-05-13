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

class ImportadorInsertarActivity : AppCompatActivity() {

    private lateinit var handler: ImportadorHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_importador_insertar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handler = ImportadorHandler(this)

        val editNUI = findViewById<EditText>(R.id.editImportadorNUI)
        val editNombre = findViewById<EditText>(R.id.editImportadorNombre)
        val editApellido = findViewById<EditText>(R.id.editImportadorApellido)
        val editApellidoCasada = findViewById<EditText>(R.id.editImportadorApellidoCasada)
        val spinnerGenero = findViewById<Spinner>(R.id.spinnerImportadorGenero)
        val editFecha = findViewById<EditText>(R.id.editImportadorFecha)
        val editDireccion = findViewById<EditText>(R.id.editImportadorDireccion)
        val editCorreo = findViewById<EditText>(R.id.editImportadorCorreo)
        val editNUIResponsable = findViewById<EditText>(R.id.editImportadorNUIResponsable)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarImportador)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiarImportador)

        // Spinner de género
        val generos = arrayOf("M", "F")
        val adapterGenero = ArrayAdapter(this, android.R.layout.simple_spinner_item, generos)
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenero.adapter = adapterGenero

        // DatePicker para fecha nacimiento
        editFecha.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                editFecha.setText("$year-${String.format("%02d", month + 1)}-${String.format("%02d", day)}")
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnGuardar.setOnClickListener {
            val nui = editNUI.text.toString().trim()
            val nombre = editNombre.text.toString().trim()
            val apellido = editApellido.text.toString().trim()
            val apellidoCasada = editApellidoCasada.text.toString().trim().ifEmpty { null }
            val genero = spinnerGenero.selectedItem.toString()
            val fecha = editFecha.text.toString().trim()
            val direccion = editDireccion.text.toString().trim()
            val correo = editCorreo.text.toString().trim()
            val nuiResponsable = editNUIResponsable.text.toString().trim().ifEmpty { null }

            if (nui.isEmpty() || nombre.isEmpty() || apellido.isEmpty() ||
                fecha.isEmpty() || direccion.isEmpty() || correo.isEmpty()) {
                Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val importador = Importador(nui, nombre, apellido, apellidoCasada,
                genero, fecha, direccion, correo, nuiResponsable)
            val resultado = handler.insertar(importador)

            if (resultado != -1L) {
                Toast.makeText(this, "Importador guardado con éxito", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            } else {
                Toast.makeText(this, "Error: NUI ya existe", Toast.LENGTH_SHORT).show()
            }
        }

        btnLimpiar.setOnClickListener { limpiarCampos() }
    }

    private fun limpiarCampos() {
        findViewById<EditText>(R.id.editImportadorNUI).setText("")
        findViewById<EditText>(R.id.editImportadorNombre).setText("")
        findViewById<EditText>(R.id.editImportadorApellido).setText("")
        findViewById<EditText>(R.id.editImportadorApellidoCasada).setText("")
        findViewById<EditText>(R.id.editImportadorFecha).setText("")
        findViewById<EditText>(R.id.editImportadorDireccion).setText("")
        findViewById<EditText>(R.id.editImportadorCorreo).setText("")
        findViewById<EditText>(R.id.editImportadorNUIResponsable).setText("")
        findViewById<EditText>(R.id.editImportadorNUI).requestFocus()
    }
}