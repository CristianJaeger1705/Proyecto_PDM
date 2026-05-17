package sv.edu.ues.fia.proyecto_pdm.gastos

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.Vehiculo
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler
import java.util.Calendar

class GastoAdicionalGestionActivity : BaseActivity() {

    private lateinit var spinnerVehiculo: Spinner
    private lateinit var spinnerHistorial: Spinner
    private lateinit var editIdGasto: EditText
    private lateinit var editConcepto: EditText
    private lateinit var editMonto: EditText
    private lateinit var editFecha: EditText
    private lateinit var btnInsertar: Button
    private lateinit var btnConsultar: Button
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnLimpiar: Button

    private lateinit var handler: GastoAdicionalHandler
    private lateinit var vehiculoHandler: VehiculoHandler

    private var listaVehiculos: List<Vehiculo> = emptyList()
    private var listaGastosActuales: List<GastoAdicional> = emptyList()
    private var gastoSeleccionado: GastoAdicional? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gasto_adicional_gestion)

        handler = GastoAdicionalHandler(this)
        vehiculoHandler = VehiculoHandler(this)

        spinnerVehiculo = findViewById(R.id.spinnerVehiculoGasto)
        spinnerHistorial = findViewById(R.id.spinnerHistorialGastos)
        editIdGasto = findViewById(R.id.editIdGasto)
        editConcepto = findViewById(R.id.editConceptoGasto)
        editMonto = findViewById(R.id.editMontoGasto)
        editFecha = findViewById(R.id.editFechaGasto)
        btnInsertar = findViewById(R.id.btnInsertarGasto)
        btnConsultar = findViewById(R.id.btnConsultarGasto)
        btnActualizar = findViewById(R.id.btnActualizarGasto)
        btnEliminar = findViewById(R.id.btnEliminarGasto)
        btnLimpiar = findViewById(R.id.btnLimpiarGasto)

        // Validar permisos (Prefix 14)
        if (!tienePermiso("141")) btnInsertar.visibility = View.GONE
        if (!tienePermiso("142")) btnActualizar.visibility = View.GONE
        if (!tienePermiso("143")) {
            btnConsultar.visibility = View.GONE
            spinnerHistorial.visibility = View.GONE
        }
        if (!tienePermiso("144")) btnEliminar.visibility = View.GONE

        cargarVehiculos()

        editFecha.setOnClickListener { mostrarDatePicker() }

        spinnerVehiculo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                listaVehiculos[position].idVehiculo?.let { cargarHistorial(it) }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerHistorial.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    limpiarCampos()
                    gastoSeleccionado = null
                } else {
                    val gasto = listaGastosActuales[position - 1]
                    cargarDatosGasto(gasto)
                    gastoSeleccionado = gasto
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnInsertar.setOnClickListener { insertar() }
        btnConsultar.setOnClickListener { consultar() }
        btnActualizar.setOnClickListener { actualizar() }
        btnEliminar.setOnClickListener { eliminar() }
        btnLimpiar.setOnClickListener {
            spinnerHistorial.setSelection(0)
            limpiarCampos()
        }
    }

    private fun mostrarDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            editFecha.setText(formattedDate)
        }, year, month, day)

        dpd.show()
    }

    private fun cargarVehiculos() {
        listaVehiculos = vehiculoHandler.obtenerTodos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaVehiculos.map { "${it.idVehiculo} - ${it.marca} ${it.modelo}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerVehiculo.adapter = adapter
    }

    private fun cargarHistorial(idVehiculo: Int) {
        listaGastosActuales = handler.obtenerGastosPorVehiculo(idVehiculo)
        val opciones = mutableListOf(getString(R.string.label_new_gasto))
        opciones.addAll(listaGastosActuales.map { "${it.idGasto}: ${it.concepto} ($${it.monto})" })

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHistorial.adapter = adapter
    }

    private fun cargarDatosGasto(gasto: GastoAdicional) {
        editIdGasto.setText(gasto.idGasto.toString())
        editConcepto.setText(gasto.concepto)
        editMonto.setText(gasto.monto.toString())
        editFecha.setText(gasto.fechaGasto)
    }

    private fun insertar() {
        if (spinnerHistorial.selectedItemPosition != 0) {
            Toast.makeText(this, getString(R.string.select_new_gasto_to_insert), Toast.LENGTH_SHORT).show()
            return
        }

        val concepto = editConcepto.text.toString()
        val montoStr = editMonto.text.toString()
        val fecha = editFecha.text.toString()
        val posVeh = spinnerVehiculo.selectedItemPosition

        if (concepto.isEmpty() || montoStr.isEmpty() || fecha.isEmpty() || posVeh == -1) {
            Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val gasto = GastoAdicional(
            idVehiculo = listaVehiculos[posVeh].idVehiculo!!,
            concepto = concepto,
            monto = montoStr.toDouble(),
            fechaGasto = fecha
        )

        val res = handler.insertar(gasto)
        if (res != -1L) {
            Toast.makeText(this, getString(R.string.gasto_guardado), Toast.LENGTH_SHORT).show()
            cargarHistorial(listaVehiculos[posVeh].idVehiculo!!)
            limpiarCampos()
        }
    }

    private fun consultar() {
        val idStr = editIdGasto.text.toString()
        if (idStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.search_id_required), Toast.LENGTH_SHORT).show()
            return
        }
        val gasto = handler.consultar(idStr.toInt())
        if (gasto != null) {
            cargarDatosGasto(gasto)
            gastoSeleccionado = gasto
            // Intentar seleccionar el vehículo en el spinner
            val index = listaVehiculos.indexOfFirst { it.idVehiculo == gasto.idVehiculo }
            if (index != -1) {
                spinnerVehiculo.setSelection(index)
            }
        } else {
            Toast.makeText(this, getString(R.string.gasto_no_encontrado), Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizar() {
        if (gastoSeleccionado == null) {
            Toast.makeText(this, getString(R.string.select_gasto_to_update), Toast.LENGTH_SHORT).show()
            return
        }

        val concepto = editConcepto.text.toString()
        val montoStr = editMonto.text.toString()
        val fecha = editFecha.text.toString()

        if (concepto.isEmpty() || montoStr.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
            return
        }

        gastoSeleccionado?.apply {
            this.concepto = concepto
            this.monto = montoStr.toDouble()
            this.fechaGasto = fecha
        }

        val res = handler.actualizar(gastoSeleccionado!!)
        if (res > 0) {
            Toast.makeText(this, getString(R.string.gasto_actualizado), Toast.LENGTH_SHORT).show()
            cargarHistorial(listaVehiculos[spinnerVehiculo.selectedItemPosition].idVehiculo!!)
        }
    }

    private fun eliminar() {
        if (gastoSeleccionado == null) {
            Toast.makeText(this, getString(R.string.select_gasto_to_delete), Toast.LENGTH_SHORT).show()
            return
        }

        val res = handler.eliminar(gastoSeleccionado!!.idGasto!!)
        if (res > 0) {
            Toast.makeText(this, getString(R.string.gasto_eliminado), Toast.LENGTH_SHORT).show()
            cargarHistorial(listaVehiculos[spinnerVehiculo.selectedItemPosition].idVehiculo!!)
            limpiarCampos()
            spinnerHistorial.setSelection(0)
        }
    }

    private fun limpiarCampos() {
        editIdGasto.text.clear()
        editConcepto.text.clear()
        editMonto.text.clear()
        editFecha.text.clear()
    }
}
