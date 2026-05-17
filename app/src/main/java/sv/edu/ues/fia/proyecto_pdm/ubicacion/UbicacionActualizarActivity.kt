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

class UbicacionActualizarActivity : BaseActivity() {

    private lateinit var ubicacionHandler: UbicacionHandler
    private lateinit var vehiculoHandler: VehiculoHandler
    private lateinit var seccionHandler: SeccionHandler
    private lateinit var bodegaHandler: BodegaHandler
    
    private lateinit var spinnerVehiculos: Spinner
    private lateinit var spinnerBodegas: Spinner
    private lateinit var spinnerSecciones: Spinner
    private lateinit var editFecha: EditText
    private lateinit var btnActualizar: Button
    
    private lateinit var listaVehiculos: List<Vehiculo>
    private lateinit var listaBodegas: List<Bodega>
    private lateinit var listaSecciones: List<Seccion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ubicacion_actualizar)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ubicacionHandler = UbicacionHandler(this)
        vehiculoHandler = VehiculoHandler(this)
        seccionHandler = SeccionHandler(this)
        bodegaHandler = BodegaHandler(this)

        spinnerVehiculos = findViewById(R.id.spinnerActualizarVehiculos)
        spinnerBodegas = findViewById(R.id.spinnerActualizarBodegas)
        spinnerSecciones = findViewById(R.id.spinnerActualizarSecciones)
        editFecha = findViewById(R.id.editActualizarFechaAsignacion)
        btnActualizar = findViewById(R.id.btnActualizarUbicacion)

        cargarDatos()

        spinnerBodegas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cargarSecciones(listaBodegas[position].idBodega)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        editFecha.setOnClickListener { mostrarDatePicker() }
        btnActualizar.setOnClickListener { procesarCambio() }
    }

    private fun cargarDatos() {
        // Filtramos solo vehículos que ya TIENEN una ubicación activa (para moverlos)
        listaVehiculos = vehiculoHandler.obtenerTodos().filter { it.idUbicacion != null }
        if (listaVehiculos.isEmpty()) {
            Toast.makeText(this, "No hay vehículos en bodegas para mover", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        val adapterVeh = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaVehiculos.map { "🚗 ID: ${it.idVehiculo} - ${it.marca}" })
        spinnerVehiculos.adapter = adapterVeh

        listaBodegas = bodegaHandler.obtenerTodas()
        val adapterBod = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaBodegas.map { it.nombreBodega })
        spinnerBodegas.adapter = adapterBod
    }

    private fun cargarSecciones(idBodega: Int) {
        listaSecciones = seccionHandler.obtenerPorBodega(idBodega)
        if (listaSecciones.isEmpty()) {
            spinnerSecciones.adapter = null
            return
        }
        val adapterSec = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaSecciones.map { "Sec: ${it.idSeccion} - Nivel ${it.nivel}" })
        spinnerSecciones.adapter = adapterSec
    }

    private fun mostrarDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, y, m, d ->
            val fecha = LocalDate.of(y, m + 1, d)
            editFecha.setText(fecha.toString())
        }, year, month, day).show()
    }

    private fun procesarCambio() {
        val fechaStr = editFecha.text.toString()
        if (fechaStr.isEmpty() || spinnerSecciones.selectedItem == null) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val vehiculoSel = listaVehiculos[spinnerVehiculos.selectedItemPosition]
            val seccionSel = listaSecciones[spinnerSecciones.selectedItemPosition]

            // 1. Liberar la ubicación vieja (técnicamente el Trigger 8 lo hacía, pero lo hacemos manual para seguridad)
            val db = sv.edu.ues.fia.proyecto_pdm.DatabaseHelper.getInstance(this).writableDatabase
            db.execSQL("UPDATE Ubicacion_Vehiculo SET Activa = 0 WHERE IdVehiculo = ?", arrayOf(vehiculoSel.idVehiculo))

            // 2. Insertar la nueva ubicación activa
            val nuevaUbicacion = Ubicacion_vehiculo(0, seccionSel.idSeccion, vehiculoSel.idVehiculo!!, LocalDate.parse(fechaStr), true)
            val idNueva = ubicacionHandler.insertar(nuevaUbicacion)

            if (idNueva != -1L) {
                // 3. Actualizar el puntero en el vehículo
                vehiculoHandler.asignarUbicacion(vehiculoSel.idVehiculo!!, idNueva.toInt())
                Toast.makeText(this, "Vehículo movido con éxito", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Error: Espacio insuficiente en la sección", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
