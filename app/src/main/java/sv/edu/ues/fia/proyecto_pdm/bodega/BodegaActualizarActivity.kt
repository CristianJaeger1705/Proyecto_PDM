package sv.edu.ues.fia.proyecto_pdm.bodega

import android.os.Bundle
import android.view.View
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

class BodegaActualizarActivity : BaseActivity() {

    private lateinit var helper: BodegaHandler
    private lateinit var spinnerId: Spinner
    private lateinit var editNombre: EditText
    private lateinit var editDepto: EditText
    private lateinit var editDir: EditText
    private lateinit var editCapacidad: EditText
    private lateinit var btnCargar: Button
    private lateinit var btnActualizar: Button
    
    private lateinit var listaBodegas: List<Bodega>
    private var bodegaActual: Bodega? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bodega_actualizar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        helper = BodegaHandler(this)
        spinnerId = findViewById(R.id.spinnerActualizarBodegaId)
        editNombre = findViewById(R.id.editActualizarNombreBodega)
        editDepto = findViewById(R.id.editActualizarDepartamento)
        editDir = findViewById(R.id.editActualizarDireccion)
        editCapacidad = findViewById(R.id.editActualizarCapacidad)
        btnCargar = findViewById(R.id.btnCargarBodega)
        btnActualizar = findViewById(R.id.btnActualizarBodega)

        cargarBodegas()
        setEdicionHabilitada(false)

        btnCargar.setOnClickListener { cargarDatosBodega() }
        btnActualizar.setOnClickListener { actualizarBodega() }
    }

    private fun cargarBodegas() {
        listaBodegas = helper.obtenerTodas()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaBodegas.map { "ID: ${it.idBodega} - ${it.nombreBodega}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerId.adapter = adapter
    }

    private fun cargarDatosBodega() {
        val pos = spinnerId.selectedItemPosition
        if (pos != -1) {
            bodegaActual = listaBodegas[pos]
            editNombre.setText(bodegaActual!!.nombreBodega)
            editDepto.setText(bodegaActual!!.departamento)
            editDir.setText(bodegaActual!!.direccion)
            editCapacidad.setText(bodegaActual!!.capacidadSecciones.toString())
            setEdicionHabilitada(true)
            Toast.makeText(this, getString(R.string.record_found), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setEdicionHabilitada(habilitar: Boolean) {
        editNombre.isEnabled = habilitar
        editDepto.isEnabled = habilitar
        editDir.isEnabled = habilitar
        // Capacidad usualmente no se edita directamente para evitar conflictos con disparadores de bodega
        btnActualizar.isEnabled = habilitar
        spinnerId.isEnabled = !habilitar
    }

    private fun actualizarBodega() {
        if (bodegaActual == null) return

        val nombre = editNombre.text.toString().trim()
        val depto = editDepto.text.toString().trim()
        val dir = editDir.text.toString().trim()

        if (nombre.isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val bodega = Bodega().apply {
            idBodega = bodegaActual!!.idBodega
            nombreBodega = nombre
            departamento = depto
            direccion = dir
            capacidadSecciones = bodegaActual!!.capacidadSecciones
        }

        val res = helper.actualizar(bodega)
        if (res > 0) {
            Toast.makeText(this, getString(R.string.update_success), Toast.LENGTH_SHORT).show()
            setEdicionHabilitada(false)
            cargarBodegas()
        } else {
            Toast.makeText(this, getString(R.string.update_error), Toast.LENGTH_SHORT).show()
        }
    }
}
