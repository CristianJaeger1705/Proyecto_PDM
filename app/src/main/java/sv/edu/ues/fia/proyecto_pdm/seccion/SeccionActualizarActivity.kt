package sv.edu.ues.fia.proyecto_pdm.seccion

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.bodega.Bodega
import sv.edu.ues.fia.proyecto_pdm.bodega.BodegaHandler

class SeccionActualizarActivity : BaseActivity() {

    private lateinit var seccionHandler: SeccionHandler
    private lateinit var bodegaHandler: BodegaHandler
    
    private lateinit var spinnerBodegas: Spinner
    private lateinit var spinnerSecciones: Spinner
    private lateinit var editNivel: EditText
    private lateinit var editCapacidad: EditText
    private lateinit var btnActualizar: Button
    
    private lateinit var listaBodegas: List<Bodega>
    private lateinit var listaSecciones: List<Seccion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seccion_actualizar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        seccionHandler = SeccionHandler(this)
        bodegaHandler = BodegaHandler(this)

        spinnerBodegas = findViewById(R.id.spinnerActualizarBodegas)
        spinnerSecciones = findViewById(R.id.spinnerActualizarSecciones)
        editNivel = findViewById(R.id.editActualizarNivel)
        editCapacidad = findViewById(R.id.editActualizarCapacidadSeccion)
        btnActualizar = findViewById(R.id.btnActualizarSeccion)

        cargarBodegas()

        spinnerBodegas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cargarSecciones(listaBodegas[position].idBodega)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerSecciones.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cargarDatosSeccion(listaSecciones[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                limpiarCampos()
            }
        }

        btnActualizar.setOnClickListener { actualizarSeccion() }
    }

    private fun cargarBodegas() {
        listaBodegas = bodegaHandler.obtenerTodas()
        if (listaBodegas.isEmpty()) {
            Toast.makeText(this, "No hay bodegas", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaBodegas.map { it.nombreBodega })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBodegas.adapter = adapter
    }

    private fun cargarSecciones(idBodega: Int) {
        listaSecciones = seccionHandler.obtenerPorBodega(idBodega)
        if (listaSecciones.isEmpty()) {
            spinnerSecciones.adapter = null
            limpiarCampos()
            return
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaSecciones.map { "ID: ${it.idSeccion} - Nivel ${it.nivel}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSecciones.adapter = adapter
    }

    private fun cargarDatosSeccion(seccion: Seccion) {
        editNivel.setText(seccion.nivel.toString())
        editCapacidad.setText(seccion.capacidadMax.toString())
    }

    private fun actualizarSeccion() {
        if (spinnerSecciones.selectedItem == null) {
            Toast.makeText(this, "Seleccione una sección", Toast.LENGTH_SHORT).show()
            return
        }

        val id = listaSecciones[spinnerSecciones.selectedItemPosition].idSeccion
        val idBodega = listaBodegas[spinnerBodegas.selectedItemPosition].idBodega
        val nivel = editNivel.text.toString().toIntOrNull()
        val capacidad = editCapacidad.text.toString().toIntOrNull()

        if (nivel != null && capacidad != null) {
            val seccion = Seccion(id, idBodega, nivel, capacidad)
            val res = seccionHandler.actualizar(seccion)
            if (res > 0) {
                Toast.makeText(this, "Sección actualizada", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Complete los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        editNivel.setText("")
        editCapacidad.setText("")
    }
}
