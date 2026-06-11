package sv.edu.ues.fia.proyecto_pdm.movimientos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.RetrofitClient
import sv.edu.ues.fia.proyecto_pdm.Vehiculo
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler
import sv.edu.ues.fia.proyecto_pdm.transporte.MedioTransporte
import sv.edu.ues.fia.proyecto_pdm.transporte.MedioTransporteHandler
import java.util.*

class WebMovimientoActivity : AppCompatActivity() {

    private lateinit var medioHandler: MedioTransporteHandler
    private lateinit var vehiculoHandler: VehiculoHandler
    private lateinit var medios: List<MedioTransporte>
    private lateinit var vehiculos: List<Vehiculo>
    private val tiposMovimiento = arrayOf("ENTRADA", "SALIDA")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_movimiento)

        medioHandler = MedioTransporteHandler(this)
        vehiculoHandler = VehiculoHandler(this)

        val spinnerMedios = findViewById<Spinner>(R.id.spinnerWebMedios)
        val spinnerTipo = findViewById<Spinner>(R.id.spinnerWebTipo)
        val editFecha = findViewById<EditText>(R.id.editWebFecha)
        val editHora = findViewById<EditText>(R.id.editWebHora)
        val editObs = findViewById<EditText>(R.id.editWebObs)
        val listVehiculos = findViewById<ListView>(R.id.listWebVehiculos)
        val btnEnviar = findViewById<Button>(R.id.btnEnviarMovimiento)

        // Cargar Medios
        medios = medioHandler.obtenerTodos()
        val adapterMedios = ArrayAdapter(this, android.R.layout.simple_spinner_item, medios.map { it.tipo })
        adapterMedios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMedios.adapter = adapterMedios

        // Cargar Tipos
        val adapterTipos = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposMovimiento)
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipo.adapter = adapterTipos

        // Cargar Vehículos
        vehiculos = vehiculoHandler.obtenerTodos()
        val adapterVehiculos = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, vehiculos.map { "${it.idVehiculo} - ${it.marca} ${it.modelo}" })
        listVehiculos.adapter = adapterVehiculos

        // Date and Time Pickers
        editFecha.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                editFecha.setText(String.format("%d-%02d-%02d", year, month + 1, day))
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        editHora.setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute ->
                editHora.setText(String.format("%02d:%02d", hour, minute))
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        val apiService = RetrofitClient.instance.create(MovimientoApiService::class.java)

        btnEnviar.setOnClickListener {
            val posMedio = spinnerMedios.selectedItemPosition
            val tipo = spinnerTipo.selectedItem.toString()
            val fecha = editFecha.text.toString()
            val hora = editHora.text.toString()
            val obs = editObs.text.toString()

            // Obtener vehículos seleccionados
            val selectedVehiculosIds = mutableListOf<Int>()
            val checked: SparseBooleanArray = listVehiculos.checkedItemPositions
            for (i in 0 until listVehiculos.count) {
                if (checked.get(i)) {
                    vehiculos[i].idVehiculo?.let { selectedVehiculosIds.add(it) }
                }
            }

            if (posMedio == -1 || fecha.isEmpty() || hora.isEmpty() || selectedVehiculosIds.isEmpty()) {
                Toast.makeText(this, "Complete los campos y seleccione al menos un vehículo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = MovimientoRequest(
                idMedio = medios[posMedio].idMedio ?: 0,
                tipoMovimiento = tipo,
                fecha = fecha,
                hora = hora,
                observaciones = obs,
                vehiculos = selectedVehiculosIds
            )

            apiService.postMovimiento(request).enqueue(object : Callback<MovimientoResponse> {
                override fun onResponse(call: Call<MovimientoResponse>, response: Response<MovimientoResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val body = response.body()!!
                        val msg = if (body.success) {
                            "Movimiento registrado. ID: ${body.idMovimiento}"
                        } else {
                            "Error: ${body.mensaje}"
                        }
                        Toast.makeText(this@WebMovimientoActivity, msg, Toast.LENGTH_LONG).show()
                        if (body.success) editObs.text.clear()
                    } else {
                        Toast.makeText(this@WebMovimientoActivity, "Error en el servidor: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<MovimientoResponse>, t: Throwable) {
                    Toast.makeText(this@WebMovimientoActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
