package sv.edu.ues.fia.proyecto_pdm.movimientos

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler

class GestionVehiculosMovimientoActivity : AppCompatActivity() {

    private lateinit var movHandler: MovimientoHandler
    private lateinit var vehHandler: VehiculoHandler
    private var idMovimiento: Int = -1
    private var idVehiculoSeleccionado: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_vehiculos_movimiento)

        movHandler = MovimientoHandler(this)
        vehHandler = VehiculoHandler(this)

        idMovimiento = intent.getIntExtra("ID_MOVIMIENTO", -1)
        if (idMovimiento == -1) {
            Toast.makeText(this, "Error: No se recibió ID de Movimiento", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val txtInfo = findViewById<TextView>(R.id.txtIdMovimientoInfo)
        txtInfo.text = "Movimiento ID: $idMovimiento"

        val editVehId = findViewById<EditText>(R.id.editVehiculoId)
        val btnAgregar = findViewById<Button>(R.id.btnAgregarVeh)
        val btnActualizar = findViewById<Button>(R.id.btnActualizarVeh)
        val btnEliminar = findViewById<Button>(R.id.btnEliminarVeh)
        val listVehiculos = findViewById<ListView>(R.id.listVehiculosMovimiento)

        cargarVehiculos(listVehiculos)

        listVehiculos.setOnItemClickListener { _, _, position, _ ->
            val id = listVehiculos.adapter.getItem(position) as Int
            idVehiculoSeleccionado = id
            editVehId.setText(id.toString())
            Toast.makeText(this, "Seleccionado ID: $id", Toast.LENGTH_SHORT).show()
        }

        btnAgregar.setOnClickListener {
            val vehIdStr = editVehId.text.toString()
            if (vehIdStr.isNotEmpty()) {
                val vehId = vehIdStr.toInt()
                val veh = vehHandler.buscar(vehId)
                if (veh != null) {
                    try {
                        val res = movHandler.agregarVehiculoAMovimiento(idMovimiento, vehId)
                        if (res != -1L) {
                            Toast.makeText(this, "Vehículo agregado", Toast.LENGTH_SHORT).show()
                            cargarVehiculos(listVehiculos)
                            editVehId.text.clear()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "El vehículo no existe", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnActualizar.setOnClickListener {
            val vehIdNuevoStr = editVehId.text.toString()
            if (idVehiculoSeleccionado != null && vehIdNuevoStr.isNotEmpty()) {
                val vehIdNuevo = vehIdNuevoStr.toInt()
                if (vehHandler.buscar(vehIdNuevo) != null) {
                    try {
                        val res = movHandler.actualizarVehiculoDeMovimiento(idMovimiento, idVehiculoSeleccionado!!, vehIdNuevo)
                        if (res > 0) {
                            Toast.makeText(this, "Asociación actualizada", Toast.LENGTH_SHORT).show()
                            cargarVehiculos(listVehiculos)
                            idVehiculoSeleccionado = null
                            editVehId.text.clear()
                        } else {
                            Toast.makeText(this, "No se pudo actualizar", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "El nuevo vehículo no existe", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Seleccione un vehículo de la lista y escriba el nuevo ID", Toast.LENGTH_SHORT).show()
            }
        }

        btnEliminar.setOnClickListener {
            if (idVehiculoSeleccionado != null) {
                val res = movHandler.eliminarVehiculoDeMovimiento(idMovimiento, idVehiculoSeleccionado!!)
                if (res > 0) {
                    Toast.makeText(this, "Vehículo eliminado del movimiento", Toast.LENGTH_SHORT).show()
                    cargarVehiculos(listVehiculos)
                    idVehiculoSeleccionado = null
                    editVehId.text.clear()
                } else {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Seleccione un vehículo de la lista", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarVehiculos(listView: ListView) {
        val vehiculos = movHandler.obtenerVehiculosDeMovimiento(idMovimiento)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, vehiculos)
        listView.adapter = adapter
    }
}
