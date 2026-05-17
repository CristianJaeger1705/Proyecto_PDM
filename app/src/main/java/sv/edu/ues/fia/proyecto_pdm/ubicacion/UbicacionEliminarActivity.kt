package sv.edu.ues.fia.proyecto_pdm.ubicacion

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.Vehiculo
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler

class UbicacionEliminarActivity : BaseActivity() {

    private lateinit var helper: UbicacionHandler
    private lateinit var vehiculoHandler: VehiculoHandler
    private lateinit var spinnerVehiculos: Spinner
    private lateinit var btnEliminar: Button
    private lateinit var listaVehiculos: List<Vehiculo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ubicacion_eliminar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        helper = UbicacionHandler(this)
        vehiculoHandler = VehiculoHandler(this)
        spinnerVehiculos = findViewById(R.id.spinnerEliminarUbicacionVehiculos)
        btnEliminar = findViewById(R.id.btnEliminarUbicacionFinal)

        cargarVehiculos()

        btnEliminar.setOnClickListener { confirmarEliminacion() }
    }

    private fun cargarVehiculos() {
        listaVehiculos = vehiculoHandler.obtenerTodos().filter { it.idUbicacion != null }
        if (listaVehiculos.isEmpty()) {
            Toast.makeText(this, "No hay vehículos con ubicación asignada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, 
            listaVehiculos.map { "🚗 ID: ${it.idVehiculo} - ${it.marca}" })
        spinnerVehiculos.adapter = adapter
    }

    private fun confirmarEliminacion() {
        if (spinnerVehiculos.selectedItem == null) return

        val vehiculo = listaVehiculos[spinnerVehiculos.selectedItemPosition]

        AlertDialog.Builder(this)
            .setTitle("Liberar Espacio")
            .setMessage("¿Desea eliminar la ubicación del vehículo '${vehiculo.marca}'? El vehículo quedará marcado como 'Sin ubicación Asignada'.")
            .setPositiveButton("Confirmar", { _, _ ->
                ejecutarEliminacion(vehiculo.idUbicacion!!)
            })
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun ejecutarEliminacion(id: Int) {
        val res = helper.eliminar(id)
        if (res > 0) {
            Toast.makeText(this, "Ubicación liberada con éxito", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al liberar ubicación", Toast.LENGTH_SHORT).show()
        }
    }
}
