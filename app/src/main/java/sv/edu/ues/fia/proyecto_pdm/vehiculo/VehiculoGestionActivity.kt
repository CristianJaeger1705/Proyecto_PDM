package sv.edu.ues.fia.proyecto_pdm.vehiculo

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.Vehiculo
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler
import sv.edu.ues.fia.proyecto_pdm.importacion.Importacion
import sv.edu.ues.fia.proyecto_pdm.importacion.ImportacionHandler

class VehiculoGestionActivity : BaseActivity() {

    private lateinit var autoIdVehiculo: AutoCompleteTextView
    private lateinit var editMarca: EditText
    private lateinit var editModelo: EditText
    private lateinit var editAnio: EditText
    private lateinit var spinnerIdImportacion: Spinner
    private lateinit var btnInsertar: Button
    private lateinit var btnConsultar: Button
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var btnIrAEstado: Button
    private lateinit var btnVerVehiculosWeb: Button

    private lateinit var vehiculoHandler: VehiculoHandler
    private lateinit var importacionHandler: ImportacionHandler
    
    private lateinit var listaImportaciones: List<Importacion>
    private lateinit var listaVehiculos: List<Vehiculo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehiculo_gestion)

        vehiculoHandler = VehiculoHandler(this)
        importacionHandler = ImportacionHandler(this)

        autoIdVehiculo = findViewById(R.id.autoIdVehiculo)
        editMarca = findViewById(R.id.editMarca)
        editModelo = findViewById(R.id.editModelo)
        editAnio = findViewById(R.id.editAnio)
        spinnerIdImportacion = findViewById(R.id.spinnerIdImportacionVehiculo)

        btnInsertar = findViewById(R.id.btnInsertarVehiculo)
        btnConsultar = findViewById(R.id.btnConsultarVehiculo)
        btnActualizar = findViewById(R.id.btnActualizarVehiculo)
        btnEliminar = findViewById(R.id.btnEliminarVehiculo)
        btnLimpiar = findViewById(R.id.btnLimpiarVehiculo)
        btnIrAEstado = findViewById(R.id.btnIrAEstadoVehicular)
        btnVerVehiculosWeb = findViewById(R.id.btnVerVehiculosWeb)

        cargarSugerencias()
        cargarImportaciones()

        // Validar permisos (Prefix 04)
        if (!tienePermiso("041")) btnInsertar.visibility = View.GONE
        if (!tienePermiso("042")) btnActualizar.visibility = View.GONE
        if (!tienePermiso("043")) btnConsultar.visibility = View.GONE
        if (!tienePermiso("044")) btnEliminar.visibility = View.GONE

        btnInsertar.setOnClickListener { insertar() }
        btnConsultar.setOnClickListener { consultar() }
        btnActualizar.setOnClickListener { actualizar() }
        btnEliminar.setOnClickListener { eliminar() }
        btnLimpiar.setOnClickListener { limpiar() }
        btnIrAEstado.setOnClickListener {
            val intent = android.content.Intent(this, sv.edu.ues.fia.proyecto_pdm.estadovehicular.EstadoVehicularGestionActivity::class.java)
            startActivity(intent)
        }
        btnVerVehiculosWeb.setOnClickListener {
            startActivity(android.content.Intent(this, VehiculosWebActivity::class.java))
        }
    }

    private fun cargarSugerencias() {
        listaVehiculos = vehiculoHandler.obtenerTodos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listaVehiculos.map { it.idVehiculo.toString() })
        autoIdVehiculo.setAdapter(adapter)
    }

    private fun cargarImportaciones() {
        listaImportaciones = importacionHandler.obtenerTodas()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaImportaciones.map { "ID: ${it.idImportacion} - ${it.idImportador}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerIdImportacion.adapter = adapter
    }

    private fun insertar() {
        val id = autoIdVehiculo.text.toString()
        val marca = editMarca.text.toString()
        val modelo = editModelo.text.toString()
        val anio = editAnio.text.toString()
        val posImp = spinnerIdImportacion.selectedItemPosition

        if (id.isEmpty() || marca.isEmpty() || modelo.isEmpty() || anio.isEmpty() || posImp == -1) {
            Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val idImp = listaImportaciones[posImp].idImportacion!!

        val vehiculo = Vehiculo(
            idVehiculo = id.toInt(),
            marca = marca,
            modelo = modelo,
            anio = anio.toInt(),
            idImportacion = idImp
        )

        val resultado = vehiculoHandler.insertar(vehiculo)
        if (resultado != -1L) {
            Toast.makeText(this, "Vehículo insertado con éxito", Toast.LENGTH_SHORT).show()
            limpiar()
            cargarSugerencias()
        } else {
            Toast.makeText(this, "Error al insertar. Posible duplicado, capacidad de importación alcanzada o antigüedad > 5 años", Toast.LENGTH_LONG).show()
        }
    }

    private fun consultar() {
        val idStr = autoIdVehiculo.text.toString()
        if (idStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.hint_enter_id), Toast.LENGTH_SHORT).show()
            return
        }

        val vehiculo = vehiculoHandler.consultar(idStr.toInt())
        if (vehiculo != null) {
            editMarca.setText(vehiculo.marca)
            editModelo.setText(vehiculo.modelo)
            editAnio.setText(vehiculo.anio.toString())
            
            // Seleccionar importación en spinner
            val index = listaImportaciones.indexOfFirst { it.idImportacion == vehiculo.idImportacion }
            if (index != -1) spinnerIdImportacion.setSelection(index)
            
            autoIdVehiculo.isEnabled = false
        } else {
            Toast.makeText(this, "Vehículo no encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizar() {
        val idStr = autoIdVehiculo.text.toString()
        val marca = editMarca.text.toString()
        val modelo = editModelo.text.toString()
        val anio = editAnio.text.toString()
        val posImp = spinnerIdImportacion.selectedItemPosition

        if (idStr.isEmpty() || marca.isEmpty() || modelo.isEmpty() || anio.isEmpty() || posImp == -1) {
            Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val idImp = listaImportaciones[posImp].idImportacion!!

        val vehiculo = Vehiculo(
            idVehiculo = idStr.toInt(),
            marca = marca,
            modelo = modelo,
            anio = anio.toInt(),
            idImportacion = idImp
        )

        val resultado = vehiculoHandler.actualizar(vehiculo)
        if (resultado > 0) {
            Toast.makeText(this, "Vehículo actualizado con éxito", Toast.LENGTH_SHORT).show()
            limpiar()
            cargarSugerencias()
        } else {
            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminar() {
        val idStr = autoIdVehiculo.text.toString()
        if (idStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.hint_enter_id), Toast.LENGTH_SHORT).show()
            return
        }

        val resultado = vehiculoHandler.eliminar(idStr.toInt())
        if (resultado > 0) {
            Toast.makeText(this, "Vehículo eliminado con éxito", Toast.LENGTH_SHORT).show()
            limpiar()
            cargarSugerencias()
        } else {
            Toast.makeText(this, "Error al eliminar. Verifique el ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiar() {
        autoIdVehiculo.setText("")
        editMarca.setText("")
        editModelo.setText("")
        editAnio.setText("")
        autoIdVehiculo.isEnabled = true
    }
}
