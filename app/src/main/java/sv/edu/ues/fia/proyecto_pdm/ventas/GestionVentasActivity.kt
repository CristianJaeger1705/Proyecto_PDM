package sv.edu.ues.fia.proyecto_pdm.ventas

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler
import sv.edu.ues.fia.proyecto_pdm.ImportadorHandler
import sv.edu.ues.fia.proyecto_pdm.Importador
import java.util.Calendar

class GestionVentasActivity : BaseActivity() {

    private lateinit var ventaHandler: VentaHandler
    private lateinit var vehiculoHandler: VehiculoHandler
    private lateinit var importadorHandler: ImportadorHandler
    private lateinit var importadores: List<Importador>
    private var ventaActual: Venta? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_ventas)

        ventaHandler = VentaHandler(this)
        vehiculoHandler = VehiculoHandler(this)
        importadorHandler = ImportadorHandler(this)

        val btnIrAInsertar = findViewById<Button>(R.id.btnIrAInsertarVenta)
        val editId = findViewById<EditText>(R.id.editVentaId)
        val editVehId = findViewById<EditText>(R.id.editVentaVehId)
        val editPrecio = findViewById<EditText>(R.id.editVentaPrecio)
        val spinnerImportadores = findViewById<Spinner>(R.id.spinnerVentaImportadores)
        val editFecha = findViewById<EditText>(R.id.editVentaFecha)
        val txtEstado = findViewById<TextView>(R.id.txtEstadoVehiculo)

        val btnBuscar = findViewById<Button>(R.id.btnBuscarVenta)
        val btnActualizar = findViewById<Button>(R.id.btnActualizarVenta)
        val btnEliminar = findViewById<Button>(R.id.btnEliminarVenta)

        // Validar permisos (Prefix 03)
        if (!tienePermiso("031")) btnIrAInsertar.visibility = View.GONE
        if (!tienePermiso("032")) btnActualizar.visibility = View.GONE
        if (!tienePermiso("033")) btnBuscar.visibility = View.GONE
        if (!tienePermiso("034")) btnEliminar.visibility = View.GONE

        // Cargar Importadores
        importadores = importadorHandler.obtenerTodos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, importadores.map { it.nombre })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerImportadores.adapter = adapter

        // DatePicker
        editFecha.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                editFecha.setText("$day/${month + 1}/$year")
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnIrAInsertar.setOnClickListener {
            val intent = Intent(this, InsertarVentaActivity::class.java)
            startActivity(intent)
        }

        btnBuscar.setOnClickListener {
            val idStr = editId.text.toString()
            if (idStr.isNotEmpty()) {
                val id = idStr.toInt()
                ventaActual = ventaHandler.buscarVenta(id)
                if (ventaActual != null) {
                    editVehId.setText(ventaActual?.idVehiculo.toString())
                    editPrecio.setText(ventaActual?.precioVenta.toString())
                    editFecha.setText(ventaActual?.fechaVenta)
                    
                    // Seleccionar importador en spinner
                    val index = importadores.indexOfFirst { it.nui == ventaActual?.nuiImportador }
                    if (index != -1) spinnerImportadores.setSelection(index)
                    
                    val veh = vehiculoHandler.consultar(ventaActual!!.idVehiculo)
                    val imp = importadorHandler.buscar(ventaActual!!.nuiImportador)
                    txtEstado.text = "Vehículo: ${veh?.marca} | Importador: ${imp?.nombre} | Estado: ${veh?.estado}"
                    Toast.makeText(this, "Venta encontrada", Toast.LENGTH_SHORT).show()
                } else {
                    txtEstado.text = ""
                    Toast.makeText(this, "No existe esa venta", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnActualizar.setOnClickListener {
            if (ventaActual != null) {
                val precio = editPrecio.text.toString().toDoubleOrNull()
                val posImp = spinnerImportadores.selectedItemPosition
                if (precio != null && posImp != -1) {
                    val nui = importadores[posImp].nui
                    val editada = Venta(
                        ventaActual!!.idVenta,
                        ventaActual!!.idVehiculo,
                        precio,
                        nui,
                        editFecha.text.toString()
                    )
                    val res = ventaHandler.actualizarVenta(editada)
                    if (res > 0) Toast.makeText(this, "Venta actualizada", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnEliminar.setOnClickListener {
            if (ventaActual != null) {
                val res = ventaHandler.eliminarVenta(ventaActual!!.idVenta)
                if (res > 0) {
                    Toast.makeText(this, "Venta eliminada y stock restaurado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
