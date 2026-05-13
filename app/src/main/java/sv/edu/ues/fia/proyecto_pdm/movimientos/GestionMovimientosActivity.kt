package sv.edu.ues.fia.proyecto_pdm.movimientos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler
import sv.edu.ues.fia.proyecto_pdm.transporte.MedioTransporteHandler
import sv.edu.ues.fia.proyecto_pdm.transporte.MedioTransporte
import java.util.Calendar

class GestionMovimientosActivity : AppCompatActivity() {

    private lateinit var movHandler: MovimientoHandler
    private lateinit var vehHandler: VehiculoHandler
    private lateinit var medioHandler: MedioTransporteHandler
    private lateinit var medios: List<MedioTransporte>
    private val tiposMovimiento = arrayOf("ENTRADA", "SALIDA")
    private var movActual: Movimiento? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_movimientos)

        movHandler = MovimientoHandler(this)
        vehHandler = VehiculoHandler(this)
        medioHandler = MedioTransporteHandler(this)

        val btnIrAInsertar = findViewById<Button>(R.id.btnIrAInsertarMov)
        val editId = findViewById<EditText>(R.id.editMovId)
        val spinnerMedios = findViewById<Spinner>(R.id.spinnerGestionMedios)
        val spinnerTipo = findViewById<Spinner>(R.id.spinnerGestionMovTipo)
        val editFecha = findViewById<EditText>(R.id.editMovFecha)
        val editHora = findViewById<EditText>(R.id.editMovHora)
        val editObs = findViewById<EditText>(R.id.editMovObs)
        val txtVehiculos = findViewById<TextView>(R.id.txtVehiculosAsignados)

        val btnBuscar = findViewById<Button>(R.id.btnBuscarMov)
        val btnActualizar = findViewById<Button>(R.id.btnActualizarMov)
        val btnEliminar = findViewById<Button>(R.id.btnEliminarMov)

        val editVehId = findViewById<EditText>(R.id.editVehIdAsignar)
        val btnAsignar = findViewById<Button>(R.id.btnAsignarVehiculo)

        // Cargar Medios
        medios = medioHandler.obtenerTodos()
        val adapterMedios = ArrayAdapter(this, android.R.layout.simple_spinner_item, medios.map { it.tipo })
        adapterMedios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMedios.adapter = adapterMedios

        // Cargar Tipos de Movimiento
        val adapterTipos = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposMovimiento)
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipo.adapter = adapterTipos

        // Pickers
        editFecha.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                editFecha.setText("$day/${month + 1}/$year")
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        editHora.setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute ->
                editHora.setText(String.format("%02d:%02d", hour, minute))
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        btnIrAInsertar.setOnClickListener {
            val intent = Intent(this, InsertarMovimientoActivity::class.java)
            startActivity(intent)
        }

        btnBuscar.setOnClickListener {
            val idStr = editId.text.toString()
            if (idStr.isNotEmpty()) {
                val id = idStr.toInt()
                movActual = movHandler.buscar(id)
                if (movActual != null) {
                    editFecha.setText(movActual?.fecha)
                    editHora.setText(movActual?.hora)
                    editObs.setText(movActual?.observaciones)
                    
                    // Seleccionar medio en spinner
                    val indexMedio = medios.indexOfFirst { it.idMedio == movActual?.idMedio }
                    if (indexMedio != -1) spinnerMedios.setSelection(indexMedio)

                    // Seleccionar tipo en spinner
                    val indexTipo = tiposMovimiento.indexOf(movActual?.tipoMovimiento)
                    if (indexTipo != -1) spinnerTipo.setSelection(indexTipo)
                    
                    actualizarListaVehiculos(id, txtVehiculos)
                    Toast.makeText(this, "Encontrado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No existe", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnActualizar.setOnClickListener {
            if (movActual != null) {
                val posMedio = spinnerMedios.selectedItemPosition
                val tipo = spinnerTipo.selectedItem.toString()
                if (posMedio != -1) {
                    val idMedio = medios[posMedio].idMedio ?: -1
                    val movEditado = Movimiento(
                        movActual!!.idMovimiento,
                        idMedio,
                        tipo,
                        editFecha.text.toString(),
                        editHora.text.toString(),
                        editObs.text.toString()
                    )
                    val res = movHandler.actualizar(movEditado)
                    if (res > 0) Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnEliminar.setOnClickListener {
            if (movActual != null) {
                val res = movHandler.eliminar(movActual!!.idMovimiento)
                if (res > 0) {
                    Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        btnAsignar.setOnClickListener {
            if (movActual != null) {
                val vehIdStr = editVehId.text.toString()
                if (vehIdStr.isNotEmpty()) {
                    val vehId = vehIdStr.toInt()
                    val veh = vehHandler.buscar(vehId)
                    if (veh != null) {
                        try {
                            val res = movHandler.agregarVehiculoAMovimiento(movActual!!.idMovimiento, vehId)
                            if (res != -1L) {
                                Toast.makeText(this, "Vehículo agregado", Toast.LENGTH_SHORT).show()
                                actualizarListaVehiculos(movActual!!.idMovimiento, txtVehiculos)
                            }
                        } catch (e: Exception) {
                            val msg = e.message ?: ""
                            if (msg.contains("Capacidad máxima", ignoreCase = true)) {
                                Toast.makeText(this, "ERROR: Se ha alcanzado el límite de capacidad", Toast.LENGTH_LONG).show()
                            } else if (msg.contains("UNIQUE constraint failed", ignoreCase = true) || msg.contains("PRIMARY KEY constraint failed", ignoreCase = true)) {
                                Toast.makeText(this, "ERROR: Este vehículo ya se agregó a este movimiento", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this, "ERROR: " + e.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "El vehículo no existe", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun actualizarListaVehiculos(movId: Int, textView: TextView) {
        val vehiculos = movHandler.obtenerVehiculosDeMovimiento(movId)
        textView.text = "Vehículos asignados (IDs): " + vehiculos.joinToString(", ")
    }
}
