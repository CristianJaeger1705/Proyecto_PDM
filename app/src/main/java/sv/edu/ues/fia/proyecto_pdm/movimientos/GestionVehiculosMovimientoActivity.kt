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
            Toast.makeText(this, getString(R.string.error_no_mov_id), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val txtInfo = findViewById<TextView>(R.id.txtIdMovimientoInfo)
        txtInfo.text = getString(R.string.mov_id_info, idMovimiento)

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
            Toast.makeText(this, getString(R.string.veh_selected, id), Toast.LENGTH_SHORT).show()
        }

        btnAgregar.setOnClickListener {
            val vehIdStr = editVehId.text.toString()
            if (vehIdStr.isNotEmpty()) {
                val vehId = vehIdStr.toInt()
                val veh = vehHandler.consultar(vehId)
                if (veh != null) {
                    try {
                        val res = movHandler.agregarVehiculoAMovimiento(idMovimiento, vehId)
                        if (res != -1L) {
                            Toast.makeText(this, getString(R.string.veh_added), Toast.LENGTH_SHORT).show()
                            cargarVehiculos(listVehiculos)
                            editVehId.text.clear()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.veh_not_exists), Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnActualizar.setOnClickListener {
            val vehIdNuevoStr = editVehId.text.toString()
            if (idVehiculoSeleccionado != null && vehIdNuevoStr.isNotEmpty()) {
                val vehIdNuevo = vehIdNuevoStr.toInt()
                if (vehHandler.consultar(vehIdNuevo) != null) {
                    try {
                        val res = movHandler.actualizarVehiculoDeMovimiento(idMovimiento, idVehiculoSeleccionado!!, vehIdNuevo)
                        if (res > 0) {
                            Toast.makeText(this, getString(R.string.association_updated), Toast.LENGTH_SHORT).show()
                            cargarVehiculos(listVehiculos)
                            idVehiculoSeleccionado = null
                            editVehId.text.clear()
                        } else {
                            Toast.makeText(this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.new_veh_not_exists), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.select_veh_error), Toast.LENGTH_SHORT).show()
            }
        }

        btnEliminar.setOnClickListener {
            if (idVehiculoSeleccionado != null) {
                val res = movHandler.eliminarVehiculoDeMovimiento(idMovimiento, idVehiculoSeleccionado!!)
                if (res > 0) {
                    Toast.makeText(this, getString(R.string.veh_deleted_from_mov), Toast.LENGTH_SHORT).show()
                    cargarVehiculos(listVehiculos)
                    idVehiculoSeleccionado = null
                    editVehId.text.clear()
                } else {
                    Toast.makeText(this, getString(R.string.delete_error), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.select_from_list), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarVehiculos(listView: ListView) {
        val vehiculos = movHandler.obtenerVehiculosDeMovimiento(idMovimiento)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, vehiculos)
        listView.adapter = adapter
    }
}
