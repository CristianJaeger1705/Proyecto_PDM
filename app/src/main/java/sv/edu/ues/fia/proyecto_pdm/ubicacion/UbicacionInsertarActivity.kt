package sv.edu.ues.fia.proyecto_pdm.ubicacion

import android.app.DatePickerDialog
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
import sv.edu.ues.fia.proyecto_pdm.Vehiculo
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler
import sv.edu.ues.fia.proyecto_pdm.bodega.Bodega
import sv.edu.ues.fia.proyecto_pdm.bodega.BodegaHandler
import sv.edu.ues.fia.proyecto_pdm.seccion.Seccion
import sv.edu.ues.fia.proyecto_pdm.seccion.SeccionHandler
import java.time.LocalDate
import java.util.Calendar

class UbicacionInsertarActivity : BaseActivity() {

    private lateinit var ubicacionHandler: UbicacionHandler
    private lateinit var vehiculoHandler: VehiculoHandler
    private lateinit var seccionHandler: SeccionHandler
    private lateinit var bodegaHandler: BodegaHandler
    
    private lateinit var spinnerBodegas: Spinner
    private lateinit var spinnerSecciones: Spinner
    private lateinit var spinnerVehiculos: Spinner
    private lateinit var editFecha: EditText
    private lateinit var btnGuardar: Button
    
    private lateinit var listaBodegas: List<Bodega>
    private lateinit var listaSecciones: List<Seccion>
    private lateinit var listaVehiculos: List<Vehiculo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ubicacion_insertar)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ubicacionHandler = UbicacionHandler(this)
        vehiculoHandler = VehiculoHandler(this)
        seccionHandler = SeccionHandler(this)
        bodegaHandler = BodegaHandler(this)

        spinnerBodegas = findViewById(R.id.spinnerUbicacionBodegas)
        spinnerSecciones = findViewById(R.id.spinnerUbicacionSecciones)
        spinnerVehiculos = findViewById(R.id.spinnerUbicacionVehiculos)
        editFecha = findViewById(R.id.editFechaAsignacion)
        btnGuardar = findViewById(R.id.btnGuardarUbicacion)

        cargarBodegas()
        cargarVehiculos()

        spinnerBodegas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cargarSecciones(listaBodegas[position].idBodega)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        editFecha.setOnClickListener { mostrarDatePicker() }
        btnGuardar.setOnClickListener { insertarUbicacion() }
    }

    private fun cargarBodegas() {
        listaBodegas = bodegaHandler.obtenerTodas()
        if (listaBodegas.isEmpty()) {
            Toast.makeText(this, "Debe registrar una bodega primero", Toast.LENGTH_SHORT).show()
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
            return
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaSecciones.map { "ID: ${it.idSeccion} - Nivel ${it.nivel}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSecciones.adapter = adapter
    }

    private fun cargarVehiculos() {
        listaVehiculos = vehiculoHandler.obtenerTodos().filter { it.estado == "DISPONIBLE" }
        if (listaVehiculos.isEmpty()) {
            Toast.makeText(this, "No hay vehículos disponibles", Toast.LENGTH_SHORT).show()
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaVehiculos.map { "ID: ${it.idVehiculo} - ${it.marca}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerVehiculos.adapter = adapter
    }

    private fun mostrarDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, y, m, d ->
            val fecha = LocalDate.of(y, m + 1, d)
            editFecha.setText(fecha.toString())
        }, year, month, day)
        dpd.show()
    }

    private fun insertarUbicacion() {
        val fechaStr = editFecha.text.toString()
        if (fechaStr.isEmpty() || spinnerSecciones.selectedItem == null || spinnerVehiculos.selectedItem == null) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val vehiculoSel = listaVehiculos[spinnerVehiculos.selectedItemPosition]
            val seccionSel = listaSecciones[spinnerSecciones.selectedItemPosition]
            
            val nuevaUbicacion = Ubicacion_vehiculo(0, seccionSel.idSeccion, vehiculoSel.idVehiculo!!, LocalDate.parse(fechaStr), true)
            val idUbicacionGen = ubicacionHandler.insertar(nuevaUbicacion)

            if (idUbicacionGen != -1L) {
                vehiculoHandler.asignarUbicacion(vehiculoSel.idVehiculo!!, idUbicacionGen.toInt())
                Toast.makeText(this, "Ubicación asignada con éxito", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Error: Capacidad de sección alcanzada", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
