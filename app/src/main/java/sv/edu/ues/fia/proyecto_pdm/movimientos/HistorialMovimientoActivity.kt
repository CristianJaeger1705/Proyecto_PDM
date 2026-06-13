package sv.edu.ues.fia.proyecto_pdm.movimientos

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.RetrofitClient
import sv.edu.ues.fia.proyecto_pdm.Vehiculo
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler

class HistorialMovimientoActivity : AppCompatActivity() {

    private lateinit var vehiculoHandler: VehiculoHandler
    private lateinit var vehiculos: List<Vehiculo>
    private lateinit var listView: ListView
    private lateinit var spinnerVehiculos: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_movimiento)

        vehiculoHandler = VehiculoHandler(this)
        spinnerVehiculos = findViewById(R.id.spinnerVehiculosHistorial)
        listView = findViewById(R.id.listHistorialMovimientos)
        val btnConsultar = findViewById<Button>(R.id.btnConsultarHistorial)

        // Cargar Vehículos locales
        vehiculos = vehiculoHandler.obtenerTodos()
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehiculos.map { "${it.idVehiculo} - ${it.marca} ${it.modelo}" })
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerVehiculos.adapter = adapterSpinner

        val apiService = RetrofitClient.instance.create(MovimientoApiService::class.java)

        btnConsultar.setOnClickListener {
            val pos = spinnerVehiculos.selectedItemPosition
            if (pos != -1) {
                val idVehiculo = vehiculos[pos].idVehiculo ?: return@setOnClickListener
                
                apiService.getHistorialVehiculo(idVehiculo).enqueue(object : Callback<HistorialVehiculoResponse> {
                    override fun onResponse(call: Call<HistorialVehiculoResponse>, response: Response<HistorialVehiculoResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            val body = response.body()!!
                            if (body.success) {
                                mostrarHistorial(body.data)
                            } else {
                                Toast.makeText(this@HistorialMovimientoActivity, getString(R.string.msg_no_history), Toast.LENGTH_SHORT).show()
                                mostrarHistorial(emptyList())
                            }
                        }
                    }

                    override fun onFailure(call: Call<HistorialVehiculoResponse>, t: Throwable) {
                        Toast.makeText(this@HistorialMovimientoActivity, "${getString(R.string.msg_connection_failure)}: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private fun mostrarHistorial(lista: List<MovimientoDetalle>) {
        val adapter = object : ArrayAdapter<MovimientoDetalle>(this, android.R.layout.simple_list_item_2, android.R.id.text1, lista) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val item = getItem(position)!!
                val text1 = view.findViewById<TextView>(android.R.id.text1)
                val text2 = view.findViewById<TextView>(android.R.id.text2)

                val tipoTraducido = when(item.tipoMovimiento.uppercase()) {
                    "ENTRADA" -> getString(R.string.mov_type_in)
                    "SALIDA" -> getString(R.string.mov_type_out)
                    else -> item.tipoMovimiento
                }
                text1.text = "$tipoTraducido - ${item.fecha} ${item.hora}"
                text2.text = "${getString(R.string.label_medium)}: ${item.medio ?: getString(R.string.label_na)}\n${getString(R.string.label_obs_short)}: ${item.observaciones}"
                
                return view
            }
        }
        listView.adapter = adapter
    }
}
