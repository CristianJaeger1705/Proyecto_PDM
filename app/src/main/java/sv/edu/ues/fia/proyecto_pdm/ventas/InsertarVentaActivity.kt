package sv.edu.ues.fia.proyecto_pdm.ventas

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler
import sv.edu.ues.fia.proyecto_pdm.ImportadorHandler
import sv.edu.ues.fia.proyecto_pdm.Importador
import java.util.Calendar

class InsertarVentaActivity : BaseActivity() {

    private lateinit var ventaHandler: VentaHandler
    private lateinit var vehiculoHandler: VehiculoHandler
    private lateinit var importadorHandler: ImportadorHandler
    private lateinit var importadores: List<Importador>
    private lateinit var vehiculos: List<sv.edu.ues.fia.proyecto_pdm.Vehiculo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_venta)

        ventaHandler = VentaHandler(this)
        vehiculoHandler = VehiculoHandler(this)
        importadorHandler = ImportadorHandler(this)

        val editId = findViewById<EditText>(R.id.editNewVentaId)
        val spinnerVehiculos = findViewById<Spinner>(R.id.spinnerNewVentaVehiculo)
        val editPrecio = findViewById<EditText>(R.id.editNewVentaPrecio)
        val spinnerImportadores = findViewById<Spinner>(R.id.spinnerImportadores)
        val editFecha = findViewById<EditText>(R.id.editNewVentaFecha)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarVenta)
        val btnGuardarWeb = findViewById<Button>(R.id.btnGuardarVentaWeb)

        // Cargar Importadores en el Spinner
        importadores = importadorHandler.obtenerTodos()
        val adapterImp = ArrayAdapter(this, android.R.layout.simple_spinner_item, importadores.map { it.nombre })
        adapterImp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerImportadores.adapter = adapterImp

        // Cargar Vehículos en el Spinner
        vehiculos = vehiculoHandler.obtenerTodos()
        val adapterVeh = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehiculos.map { "${it.idVehiculo} - ${it.marca} ${it.modelo}" })
        adapterVeh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerVehiculos.adapter = adapterVeh

        // Configurar DatePicker
        editFecha.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                editFecha.setText("$day/${month + 1}/$year")
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnGuardar.setOnClickListener {
            val idVenta = editId.text.toString().toIntOrNull()
            val posVeh = spinnerVehiculos.selectedItemPosition
            val precio = editPrecio.text.toString().toDoubleOrNull()
            val fecha = editFecha.text.toString()
            
            val posImportador = spinnerImportadores.selectedItemPosition
            
            if (idVenta != null && posVeh != -1 && precio != null && posImportador != -1 && fecha.isNotEmpty()) {
                val nui = importadores[posImportador].nui
                val veh = vehiculos[posVeh]
                val idVeh = veh.idVehiculo!!
                
                if (veh.estado == "DISPONIBLE") {
                    val nuevaVenta = Venta(idVenta, idVeh, precio, nui, fecha)
                    val res = ventaHandler.registrarVenta(nuevaVenta)
                    if (res != -1L) {
                        Toast.makeText(this, getString(R.string.msg_sale_success), Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, getString(R.string.msg_sale_id_exists), Toast.LENGTH_SHORT).show()
                    }
                } else if (veh.estado == "EN_REPARACION") {
                    Toast.makeText(this, getString(R.string.msg_sale_reparacion_error), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, getString(R.string.msg_not_found), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
            }
        }

        btnGuardarWeb.setOnClickListener {
            val idVenta = editId.text.toString().toIntOrNull()
            val posVeh = spinnerVehiculos.selectedItemPosition
            val precio = editPrecio.text.toString().toDoubleOrNull()
            val fecha = editFecha.text.toString()
            val posImp = spinnerImportadores.selectedItemPosition

            if (idVenta != null && posVeh != -1 && precio != null && posImp != -1 && fecha.isNotEmpty()) {
                val venta = Venta(idVenta, vehiculos[posVeh].idVehiculo!!, precio, importadores[posImp].nui, fecha)
                val apiService = sv.edu.ues.fia.proyecto_pdm.RetrofitClient.instance.create(VentaApiService::class.java)
                
                apiService.registrarVenta(venta).enqueue(object : retrofit2.Callback<VentaResponse> {
                    override fun onResponse(call: retrofit2.Call<VentaResponse>, response: retrofit2.Response<VentaResponse>) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            Toast.makeText(this@InsertarVentaActivity, "Venta registrada en WEB con éxito", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@InsertarVentaActivity, "Error en WEB: ${response.body()?.mensaje}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: retrofit2.Call<VentaResponse>, t: Throwable) {
                        Toast.makeText(this@InsertarVentaActivity, "Fallo de conexión WEB: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
