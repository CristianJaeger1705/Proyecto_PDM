package sv.edu.ues.fia.proyecto_pdm.ventas

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler
import sv.edu.ues.fia.proyecto_pdm.ImportadorHandler
import sv.edu.ues.fia.proyecto_pdm.Importador
import java.util.Calendar

class InsertarVentaActivity : AppCompatActivity() {

    private lateinit var ventaHandler: VentaHandler
    private lateinit var vehiculoHandler: VehiculoHandler
    private lateinit var importadorHandler: ImportadorHandler
    private lateinit var importadores: List<Importador>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_venta)

        ventaHandler = VentaHandler(this)
        vehiculoHandler = VehiculoHandler(this)
        importadorHandler = ImportadorHandler(this)

        val editId = findViewById<EditText>(R.id.editNewVentaId)
        val editVehId = findViewById<EditText>(R.id.editNewVentaVehId)
        val editPrecio = findViewById<EditText>(R.id.editNewVentaPrecio)
        val spinnerImportadores = findViewById<Spinner>(R.id.spinnerImportadores)
        val editFecha = findViewById<EditText>(R.id.editNewVentaFecha)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarVenta)

        // Cargar Importadores en el Spinner
        importadores = importadorHandler.obtenerTodos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, importadores.map { it.nombre })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerImportadores.adapter = adapter

        // Configurar DatePicker
        editFecha.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                editFecha.setText("$day/${month + 1}/$year")
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnGuardar.setOnClickListener {
            val idVenta = editId.text.toString().toIntOrNull()
            val idVeh = editVehId.text.toString().toIntOrNull()
            val precio = editPrecio.text.toString().toDoubleOrNull()
            val fecha = editFecha.text.toString()
            
            val posImportador = spinnerImportadores.selectedItemPosition
            
            if (idVenta != null && idVeh != null && precio != null && posImportador != -1 && fecha.isNotEmpty()) {
                val nui = importadores[posImportador].nui
                val veh = vehiculoHandler.buscar(idVeh)
                if (veh != null) {
                    if (veh.estado == "DISPONIBLE") {
                        val nuevaVenta = Venta(idVenta, idVeh, precio, nui, fecha)
                        val res = ventaHandler.registrarVenta(nuevaVenta)
                        if (res != -1L) {
                            Toast.makeText(this, "Venta registrada con éxito", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Error: ID Venta ya existe", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "El vehículo ya está VENDIDO", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "El vehículo no existe", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
