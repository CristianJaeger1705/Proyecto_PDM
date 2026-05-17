package sv.edu.ues.fia.proyecto_pdm.ubicacion

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.Vehiculo
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler

class UbicacionConsultarActivity : BaseActivity() {

    private lateinit var ubicacionHandler: UbicacionHandler
    private lateinit var vehiculoHandler: VehiculoHandler
    
    private lateinit var spinnerVehiculos: Spinner
    private lateinit var textResultado: TextView
    private lateinit var btnConsultar: Button
    
    private lateinit var listaVehiculos: List<Vehiculo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ubicacion_consultar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ubicacionHandler = UbicacionHandler(this)
        vehiculoHandler = VehiculoHandler(this)

        spinnerVehiculos = findViewById(R.id.spinnerConsultarVehiculos)
        textResultado = findViewById(R.id.textResultadoUbicacion)
        btnConsultar = findViewById(R.id.btnConsultarUbicacionVehiculo)

        cargarVehiculos()

        btnConsultar.setOnClickListener { consultarUbicacion() }
    }

    private fun cargarVehiculos() {
        // Obtenemos todos los vehículos para poder buscar la ubicación de cualquiera
        listaVehiculos = vehiculoHandler.obtenerTodos()
        if (listaVehiculos.isEmpty()) {
            Toast.makeText(this, "No hay vehículos registrados", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, 
            listaVehiculos.map { "ID: ${it.idVehiculo} - ${it.marca ?: "S/M"} ${it.modelo ?: ""}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerVehiculos.adapter = adapter
    }

    private fun consultarUbicacion() {
        val vehiculoSel = listaVehiculos[spinnerVehiculos.selectedItemPosition]
        
        val info = ubicacionHandler.buscarPorVehiculo(vehiculoSel.idVehiculo!!)
        
        if (info != null) {
            val res = StringBuilder()
            res.append("UBICACIÓN ENCONTRADA:\n\n")
            res.append("🚗 Vehículo: ${vehiculoSel.marca} ${vehiculoSel.modelo}\n")
            res.append("🏢 Bodega: ${info["bodega"]}\n")
            res.append("📏 Nivel: ${info["nivel"]}\n")
            res.append("📅 Asignado: ${info["fecha"]}\n")
            res.append("🆔 Ref. Ubicación: ${info["idUbicacion"]}")
            
            textResultado.text = res.toString()
        } else {
            textResultado.text = "El vehículo seleccionado no tiene una ubicación asignada actualmente."
        }
    }
}
