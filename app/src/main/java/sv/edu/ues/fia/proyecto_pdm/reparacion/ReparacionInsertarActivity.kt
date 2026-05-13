package sv.edu.ues.fia.proyecto_pdm.reparacion

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.Vehiculo
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler
import sv.edu.ues.fia.proyecto_pdm.taller.Taller
import sv.edu.ues.fia.proyecto_pdm.taller.TallerHandler

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
        val editApto = findViewById<EditText>(R.id.editInsRepApto)
        val editCosto = findViewById<EditText>(R.id.editInsRepCosto)
        
        val btnGuardar = findViewById<Button>(R.id.btnInsRepGuardar)
        val btnLimpiar = findViewById<Button>(R.id.btnInsRepLimpiar)

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
            val apto = editApto.text.toString()
            val costoStr = editCosto.text.toString()

            if (posTaller != -1 && posVehiculo != -1) {
                val reparacion = Reparacion(
                    idReparacion = 0, // Auto-generado
                    idTaller = talleres[posTaller].idTaller,
                    idVehiculo = vehiculos[posVehiculo].idVehiculo,
                    fechaEntrada = fechaEntrada,
                    fechaSalida = null,
                    descripcionTrabajo = descripcion,
                    aptoParaVenta = apto,
                    costo = costoStr.toDoubleOrNull() ?: 0.0,
                )
                
                val resultado = handler.insertar(reparacion)
                if (resultado != -1L) {
                    Toast.makeText(this, "Reparación guardada (ID: $resultado)", Toast.LENGTH_SHORT).show()
                    limpiar()
                } else {
                    Toast.makeText(this, "Error al insertar.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Debe seleccionar un taller y un vehículo", Toast.LENGTH_SHORT).show()
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
        findViewById<EditText>(R.id.editInsRepApto).text.clear()
        findViewById<EditText>(R.id.editInsRepCosto).text.clear()
    }
}
