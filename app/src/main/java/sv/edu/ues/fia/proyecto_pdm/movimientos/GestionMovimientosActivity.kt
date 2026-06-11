package sv.edu.ues.fia.proyecto_pdm.movimientos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler
import sv.edu.ues.fia.proyecto_pdm.transporte.MedioTransporteHandler
import sv.edu.ues.fia.proyecto_pdm.transporte.MedioTransporte
import java.util.Calendar

class GestionMovimientosActivity : BaseActivity() {

    private lateinit var movHandler: MovimientoHandler
    private lateinit var vehHandler: VehiculoHandler
    private lateinit var medioHandler: MedioTransporteHandler
    private lateinit var medios: List<MedioTransporte>
    private lateinit var movimientos: List<Movimiento>
    private val tiposMovimiento = arrayOf("ENTRADA", "SALIDA")
    private var movActual: Movimiento? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_movimientos)

        movHandler = MovimientoHandler(this)
        vehHandler = VehiculoHandler(this)
        medioHandler = MedioTransporteHandler(this)

        val btnIrAInsertar = findViewById<Button>(R.id.btnIrAInsertarMov)
        val btnIrAInsertarWeb = findViewById<Button>(R.id.btnIrAInsertarWebMov)
        val spinnerMovimientos = findViewById<Spinner>(R.id.spinnerGestionMovId)
        val spinnerMedios = findViewById<Spinner>(R.id.spinnerGestionMedios)
        val spinnerTipo = findViewById<Spinner>(R.id.spinnerGestionMovTipo)
        val editFecha = findViewById<EditText>(R.id.editMovFecha)
        val editHora = findViewById<EditText>(R.id.editMovHora)
        val editObs = findViewById<EditText>(R.id.editMovObs)
        val txtVehiculos = findViewById<TextView>(R.id.txtVehiculosAsignados)
        val txtMovStatus = findViewById<TextView>(R.id.txtMovStatus)
        val layoutStatus = findViewById<LinearLayout>(R.id.layoutStatus)

        val btnBuscar = findViewById<Button>(R.id.btnBuscarMov)
        val btnActualizar = findViewById<Button>(R.id.btnActualizarMov)
        val btnEliminar = findViewById<Button>(R.id.btnEliminarMov)

        // Validar permisos (Prefix 11)
        if (!tienePermiso("111")) {
            btnIrAInsertar.visibility = View.GONE
            btnIrAInsertarWeb.visibility = View.GONE
        }
        if (!tienePermiso("112")) btnActualizar.visibility = View.GONE
        if (!tienePermiso("113")) btnBuscar.visibility = View.GONE
        if (!tienePermiso("114")) btnEliminar.visibility = View.GONE

        val btnAsignar = findViewById<Button>(R.id.btnAsignarVehiculo)
        // También restringir asignación si no puede editar? 
        // El usuario dijo "solo consultar para consultor", así que 112 y 114 están fuera.
        // Asignar vehículos a un movimiento es una edición.
        if (!tienePermiso("112")) btnAsignar.visibility = View.GONE

        // Cargar Medios
        medios = medioHandler.obtenerTodos()
        val adapterMedios = ArrayAdapter(this, android.R.layout.simple_spinner_item, medios.map { it.tipo })
        adapterMedios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMedios.adapter = adapterMedios

        // Cargar Tipos de Movimiento
        val adapterTipos = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposMovimiento)
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipo.adapter = adapterTipos

        cargarMovimientos(spinnerMovimientos)

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

        btnIrAInsertarWeb.setOnClickListener {
            val intent = Intent(this, WebMovimientoActivity::class.java)
            startActivity(intent)
        }

        btnBuscar.setOnClickListener {
            val pos = spinnerMovimientos.selectedItemPosition
            if (pos != -1) {
                movActual = movimientos[pos]
                val id = movActual!!.idMovimiento
                
                editFecha.setText(movActual?.fecha)
                editHora.setText(movActual?.hora)
                editObs.setText(movActual?.observaciones)
                
                // Seleccionar medio en spinner
                val indexMedio = medios.indexOfFirst { it.idMedio == movActual?.idMedio }
                if (indexMedio != -1) spinnerMedios.setSelection(indexMedio)

                // Seleccionar tipo en spinner
                val indexTipo = tiposMovimiento.indexOf(movActual?.tipoMovimiento)
                if (indexTipo != -1) spinnerTipo.setSelection(indexTipo)
                
                // Mostrar estado de autorización
                layoutStatus.visibility = View.VISIBLE
                if (movActual!!.autorizado == 1) {
                    txtMovStatus.text = getString(R.string.status_authorized)
                    txtMovStatus.setTextColor(resources.getColor(R.color.auto_import_dark, null))
                } else {
                    txtMovStatus.text = getString(R.string.status_pending)
                    txtMovStatus.setTextColor(resources.getColor(R.color.auto_import_red, null))
                }
                
                actualizarListaVehiculos(id, txtVehiculos)
                Toast.makeText(this, getString(R.string.mov_found), Toast.LENGTH_SHORT).show()
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
                    if (res > 0) Toast.makeText(this, getString(R.string.mov_update_success), Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnEliminar.setOnClickListener {
            if (movActual != null) {
                val res = movHandler.eliminar(movActual!!.idMovimiento)
                if (res > 0) {
                    Toast.makeText(this, getString(R.string.mov_delete_success), Toast.LENGTH_SHORT).show()
                    cargarMovimientos(spinnerMovimientos)
                    limpiarCampos()
                }
            }
        }

        btnAsignar.setOnClickListener {
            if (movActual != null) {
                val intent = Intent(this, GestionVehiculosMovimientoActivity::class.java)
                intent.putExtra("ID_MOVIMIENTO", movActual!!.idMovimiento)
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.mov_search_required), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actualizarListaVehiculos(movId: Int, textView: TextView) {
        val vehiculos = movHandler.obtenerVehiculosDeMovimiento(movId)
        textView.text = getString(R.string.vehicles_label) + vehiculos.joinToString(", ")
    }

    private fun cargarMovimientos(spinner: Spinner) {
        movimientos = movHandler.obtenerTodos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, movimientos.map { "#${it.idMovimiento} - ${it.tipoMovimiento} (${it.fecha})" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun limpiarCampos() {
        movActual = null
        findViewById<EditText>(R.id.editMovFecha).text.clear()
        findViewById<EditText>(R.id.editMovHora).text.clear()
        findViewById<EditText>(R.id.editMovObs).text.clear()
        findViewById<LinearLayout>(R.id.layoutStatus).visibility = View.GONE
        findViewById<TextView>(R.id.txtVehiculosAsignados).text = getString(R.string.vehicles_label)
    }

    override fun onResume() {
        super.onResume()
        // Recargar movimientos
        val spinnerMovimientos = findViewById<Spinner>(R.id.spinnerGestionMovId)
        cargarMovimientos(spinnerMovimientos)

        // Actualizar la lista de vehículos si ya hay un movimiento buscado
        movActual?.let {
            val txtVehiculos = findViewById<TextView>(R.id.txtVehiculosAsignados)
            actualizarListaVehiculos(it.idMovimiento, txtVehiculos)
        }
    }
}
