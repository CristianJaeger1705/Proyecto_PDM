package sv.edu.ues.fia.proyecto_pdm.reparacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
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

class ReparacionGestionActivity : BaseActivity() {

    private lateinit var handler: ReparacionHandler
    private lateinit var tallerHandler: TallerHandler
    private lateinit var vehiculoHandler: VehiculoHandler

    private lateinit var talleres: List<Taller>
    private lateinit var vehiculos: List<Vehiculo>

    private var reparacionActual: Reparacion? = null
    private lateinit var adapter: ReparacionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reparacion_gestion)

        handler = ReparacionHandler(this)
        tallerHandler = TallerHandler(this)
        vehiculoHandler = VehiculoHandler(this)

        val editBusquedaId = findViewById<EditText>(R.id.editBusquedaRepId)
        val spinnerTaller = findViewById<Spinner>(R.id.spinnerRepTaller)
        val spinnerVehiculo = findViewById<Spinner>(R.id.spinnerRepVehiculo)
        val editFechaEntrada = findViewById<EditText>(R.id.editRepFechaEntrada)
        val editFechaSalida = findViewById<EditText>(R.id.editRepFechaSalida)
        val editDescripcion = findViewById<EditText>(R.id.editRepDescripcion)
        val editApto = findViewById<EditText>(R.id.editRepApto)
        val editCosto = findViewById<EditText>(R.id.editRepCosto)

        val btnIrACrear = findViewById<Button>(R.id.btnIrACrearRep)
        val btnBuscar = findViewById<Button>(R.id.btnBuscarRep)
        val btnActualizar = findViewById<Button>(R.id.btnActualizarRep)
        val btnEliminar = findViewById<Button>(R.id.btnEliminarRep)

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
            editBusquedaId.setText(reparacion.idReparacion.toString())
        }
        recyclerReparaciones.layoutManager = LinearLayoutManager(this)
        recyclerReparaciones.adapter = adapter

        actualizarLista()

        btnIrACrear.setOnClickListener {
            val intent = Intent(this, ReparacionInsertarActivity::class.java)
            startActivity(intent)
        }

        btnBuscar.setOnClickListener {
            val idStr = editBusquedaId.text.toString()
            if (idStr.isNotEmpty()) {
                val id = idStr.toInt()
                reparacionActual = handler.buscar(id)
                
                if (reparacionActual != null) {
                    llenarCampos(reparacionActual!!, spinnerTaller, spinnerVehiculo)
                    Toast.makeText(this, "Registro encontrado", Toast.LENGTH_SHORT).show()
                } else {
                    limpiarCampos()
                    Toast.makeText(this, "No se encontró el ID: $id", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Ingrese un ID para buscar", Toast.LENGTH_SHORT).show()
            }
        }

        btnActualizar.setOnClickListener {
            if (reparacionActual != null) {
                val posTaller = spinnerTaller.selectedItemPosition
                val posVehiculo = spinnerVehiculo.selectedItemPosition
                val fechaEntrada = editFechaEntrada.text.toString()
                val fechaSalida = editFechaSalida.text.toString()
                val descripcion = editDescripcion.text.toString()
                val apto = editApto.text.toString()
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
                    editBusquedaId.text.clear()
                    reparacionActual = null
                    actualizarLista()
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
        findViewById<EditText>(R.id.editRepApto).text.clear()
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
        findViewById<EditText>(R.id.editRepApto).setText(rep.aptoParaVenta)
        findViewById<EditText>(R.id.editRepCosto).setText(rep.costo.toString())
    }

    private fun actualizarLista() {
        val reparaciones = handler.obtenerTodos()
        adapter.updateList(reparaciones)
    }
}
