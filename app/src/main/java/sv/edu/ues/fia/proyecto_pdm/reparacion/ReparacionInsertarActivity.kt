package sv.edu.ues.fia.proyecto_pdm.reparacion

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.Vehiculo
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler
import sv.edu.ues.fia.proyecto_pdm.taller.Taller
import sv.edu.ues.fia.proyecto_pdm.taller.TallerHandler
import java.util.Calendar
import java.util.Locale

class ReparacionInsertarActivity : AppCompatActivity() {

    private lateinit var handler: ReparacionHandler
    private lateinit var tallerHandler: TallerHandler
    private lateinit var vehiculoHandler: VehiculoHandler
    
    private lateinit var talleres: List<Taller>
    private lateinit var vehiculos: List<Vehiculo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reparacion_insertar)

        handler = ReparacionHandler(this)
        tallerHandler = TallerHandler(this)
        vehiculoHandler = VehiculoHandler(this)

        val spinnerTaller = findViewById<Spinner>(R.id.spinnerRepTaller)
        val spinnerVehiculo = findViewById<Spinner>(R.id.spinnerRepVehiculo)
        val editFechaEntrada = findViewById<EditText>(R.id.editInsRepFechaEntrada)
        val editDescripcion = findViewById<EditText>(R.id.editInsRepDescripcion)
        val checkApto = findViewById<CheckBox>(R.id.checkInsRepApto)
        val editCosto = findViewById<EditText>(R.id.editInsRepCosto)
        
        val btnGuardar = findViewById<Button>(R.id.btnInsRepGuardar)
        val btnLimpiar = findViewById<Button>(R.id.btnInsRepLimpiar)

        // DatePicker para fecha entrada
        editFechaEntrada.setOnClickListener {
            mostrarDatePicker(editFechaEntrada)
        }

        // Cargar Talleres
        talleres = tallerHandler.obtenerTodos()
        val adapterTalleres = ArrayAdapter(this, android.R.layout.simple_spinner_item, talleres.map { it.nombreTaller })
        adapterTalleres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTaller.adapter = adapterTalleres

        // Cargar Vehículos
        vehiculos = vehiculoHandler.obtenerTodos()
        val adapterVehiculos = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehiculos.map { "${it.idVehiculo} - ${it.marca}" })
        adapterVehiculos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerVehiculo.adapter = adapterVehiculos

        btnGuardar.setOnClickListener {
            val posTaller = spinnerTaller.selectedItemPosition
            val posVehiculo = spinnerVehiculo.selectedItemPosition
            val fechaEntrada = editFechaEntrada.text.toString()
            val descripcion = editDescripcion.text.toString()
            val apto = if (checkApto.isChecked) "S" else "N"
            val costoStr = editCosto.text.toString()

            if (posTaller != -1 && posVehiculo != -1) {
                val reparacion = Reparacion(
                    idReparacion = 0, // Auto-generado
                    idTaller = talleres[posTaller].idTaller,
                    idVehiculo = vehiculos[posVehiculo].idVehiculo!!,
                    fechaEntrada = fechaEntrada,
                    fechaSalida = null,
                    descripcionTrabajo = descripcion,
                    aptoParaVenta = apto,
                    costo = costoStr.toDoubleOrNull() ?: 0.0,
                )
                
                val resultado = handler.insertar(reparacion)
                if (resultado != -1L) {
                    Toast.makeText(this, getString(R.string.msg_repair_saved, resultado), Toast.LENGTH_SHORT).show()
                    limpiar()
                } else {
                    Toast.makeText(this, getString(R.string.insert_error), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.msg_select_workshop_vehicle), Toast.LENGTH_SHORT).show()
            }
        }

        btnLimpiar.setOnClickListener {
            limpiar()
        }
    }

    private fun limpiar() {
        // findViewById<EditText>(R.id.editInsRepId).text.clear() // Eliminado
        findViewById<EditText>(R.id.editInsRepFechaEntrada).text.clear()
        findViewById<EditText>(R.id.editInsRepDescripcion).text.clear()
        findViewById<CheckBox>(R.id.checkInsRepApto).isChecked = false
        findViewById<EditText>(R.id.editInsRepCosto).text.clear()
    }

    private fun mostrarDatePicker(editText: EditText) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format(Locale.US, "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            editText.setText(formattedDate)
        }, year, month, day)

        dpd.show()
    }
}
