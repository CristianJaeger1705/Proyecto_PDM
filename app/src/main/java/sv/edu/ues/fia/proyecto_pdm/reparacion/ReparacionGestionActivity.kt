package sv.edu.ues.fia.proyecto_pdm.reparacion

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.Vehiculo
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler
import sv.edu.ues.fia.proyecto_pdm.taller.Taller
import sv.edu.ues.fia.proyecto_pdm.taller.TallerHandler
import java.util.Calendar
import java.util.Locale

class ReparacionGestionActivity : BaseActivity() {

    private lateinit var handler: ReparacionHandler
    private lateinit var tallerHandler: TallerHandler
    private lateinit var vehiculoHandler: VehiculoHandler

    private lateinit var talleres: List<Taller>
    private lateinit var vehiculos: List<Vehiculo>
    private lateinit var reparaciones: List<Reparacion>

    private var reparacionActual: Reparacion? = null
    private lateinit var adapter: ReparacionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reparacion_gestion)

        handler = ReparacionHandler(this)
        tallerHandler = TallerHandler(this)
        vehiculoHandler = VehiculoHandler(this)

        val spinnerRepId = findViewById<Spinner>(R.id.spinnerGestionRepId)
        val spinnerTaller = findViewById<Spinner>(R.id.spinnerRepTaller)
        val spinnerVehiculo = findViewById<Spinner>(R.id.spinnerRepVehiculo)
        val editFechaEntrada = findViewById<EditText>(R.id.editRepFechaEntrada)
        val editFechaSalida = findViewById<EditText>(R.id.editRepFechaSalida)
        val editDescripcion = findViewById<EditText>(R.id.editRepDescripcion)
        val checkApto = findViewById<CheckBox>(R.id.checkRepApto)
        val editCosto = findViewById<EditText>(R.id.editRepCosto)

        val btnIrACrear = findViewById<Button>(R.id.btnIrACrearRep)
        val btnBuscar = findViewById<Button>(R.id.btnBuscarRep)
        val btnActualizar = findViewById<Button>(R.id.btnActualizarRep)
        val btnEliminar = findViewById<Button>(R.id.btnEliminarRep)

        // DatePickers
        editFechaEntrada.setOnClickListener { mostrarDatePicker(editFechaEntrada) }
        editFechaSalida.setOnClickListener { mostrarDatePicker(editFechaSalida) }

        // Validar permisos (Prefix 10)
        if (!tienePermiso("101")) btnIrACrear.visibility = View.GONE
        if (!tienePermiso("102")) btnActualizar.visibility = View.GONE
        if (!tienePermiso("103")) btnBuscar.visibility = View.GONE
        if (!tienePermiso("104")) btnEliminar.visibility = View.GONE

        val recyclerReparaciones = findViewById<RecyclerView>(R.id.recyclerReparaciones)

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

        // Configurar RecyclerView
        adapter = ReparacionAdapter(emptyList()) { reparacion ->
            llenarCampos(reparacion, spinnerTaller, spinnerVehiculo)
            reparacionActual = reparacion
            val index = reparaciones.indexOfFirst { it.idReparacion == reparacion.idReparacion }
            if (index != -1) spinnerRepId.setSelection(index)
        }
        recyclerReparaciones.layoutManager = LinearLayoutManager(this)
        recyclerReparaciones.adapter = adapter

        actualizarLista()
        cargarReparaciones(spinnerRepId)

        btnIrACrear.setOnClickListener {
            val intent = Intent(this, ReparacionInsertarActivity::class.java)
            startActivity(intent)
        }

        btnBuscar.setOnClickListener {
            val pos = spinnerRepId.selectedItemPosition
            if (pos != -1) {
                reparacionActual = reparaciones[pos]
                llenarCampos(reparacionActual!!, spinnerTaller, spinnerVehiculo)
                Toast.makeText(this, "Registro encontrado", Toast.LENGTH_SHORT).show()
            }
        }

        btnActualizar.setOnClickListener {
            if (reparacionActual != null) {
                val posTaller = spinnerTaller.selectedItemPosition
                val posVehiculo = spinnerVehiculo.selectedItemPosition
                val fechaEntrada = editFechaEntrada.text.toString()
                val fechaSalida = editFechaSalida.text.toString()
                val descripcion = editDescripcion.text.toString()
                val apto = if (checkApto.isChecked) "S" else "N"
                val costo = editCosto.text.toString().toDoubleOrNull() ?: 0.0

                if (posTaller != -1 && posVehiculo != -1) {
                    val editada = Reparacion(
                        idReparacion = reparacionActual!!.idReparacion,
                        idTaller = talleres[posTaller].idTaller,
                        idVehiculo = vehiculos[posVehiculo].idVehiculo!!,
                        fechaEntrada = fechaEntrada,
                        fechaSalida = fechaSalida.ifEmpty { null },
                        descripcionTrabajo = descripcion,
                        aptoParaVenta = apto,
                        costo = costo,
                    )
                    val filas = handler.actualizar(editada)
                    if (filas > 0) {
                        Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
                        actualizarLista()
                        cargarReparaciones(spinnerRepId)
                    } else {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Debe seleccionar un taller y un vehículo", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Primero busque un registro", Toast.LENGTH_SHORT).show()
            }
        }

        btnEliminar.setOnClickListener {
            if (reparacionActual != null) {
                val id = reparacionActual!!.idReparacion
                val filas = handler.eliminar(id)
                if (filas > 0) {
                    Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                    reparacionActual = null
                    actualizarLista()
                    cargarReparaciones(spinnerRepId)
                } else {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Primero busque un registro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarLista()
    }

    private fun limpiarCampos() {
        // Los Spinners se quedan como están o podrías resetearlos a la pos 0
        findViewById<EditText>(R.id.editRepFechaEntrada).text.clear()
        findViewById<EditText>(R.id.editRepFechaSalida).text.clear()
        findViewById<EditText>(R.id.editRepDescripcion).text.clear()
        findViewById<CheckBox>(R.id.checkRepApto).isChecked = false
        findViewById<EditText>(R.id.editRepCosto).text.clear()
    }

    private fun llenarCampos(rep: Reparacion, spinnerTaller: Spinner, spinnerVehiculo: Spinner) {
        val indexTaller = talleres.indexOfFirst { it.idTaller == rep.idTaller }
        if (indexTaller != -1) spinnerTaller.setSelection(indexTaller)

        val indexVehiculo = vehiculos.indexOfFirst { it.idVehiculo == rep.idVehiculo }
        if (indexVehiculo != -1) spinnerVehiculo.setSelection(indexVehiculo)

        findViewById<EditText>(R.id.editRepFechaEntrada).setText(rep.fechaEntrada)
        findViewById<EditText>(R.id.editRepFechaSalida).setText(rep.fechaSalida ?: "")
        findViewById<EditText>(R.id.editRepDescripcion).setText(rep.descripcionTrabajo)
        findViewById<CheckBox>(R.id.checkRepApto).isChecked = rep.aptoParaVenta.trim().uppercase() == "S"
        findViewById<EditText>(R.id.editRepCosto).setText(rep.costo.toString())
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

    private fun actualizarLista() {
        val reparacionesList = handler.obtenerTodos()
        adapter.updateList(reparacionesList)
    }

    private fun cargarReparaciones(spinner: Spinner) {
        reparaciones = handler.obtenerTodos()
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, reparaciones.map { "#${it.idReparacion} - Veh:${it.idVehiculo} (${it.fechaEntrada})" })
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner
    }
}
