package sv.edu.ues.fia.proyecto_pdm.vehiculo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.RetrofitClient

class VehiculosWebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_vehiculos)

        val spinnerFiltro = findViewById<Spinner>(R.id.spinnerFiltroEstado)
        val btnCargar = findViewById<Button>(R.id.btnCargarVehiculos)
        val txtResultados = findViewById<TextView>(R.id.txtResultadosVehiculos)

        // Configurar el spinner
        val opciones = arrayOf("Todos", "DISPONIBLE", "VENDIDO")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFiltro.adapter = adapter

        val apiService = RetrofitClient.instance.create(VehiculoApiService::class.java)

        btnCargar.setOnClickListener {
            txtResultados.text = "Cargando..."

            val seleccion = spinnerFiltro.selectedItem.toString()
            val estadoQuery = if (seleccion == "Todos") null else seleccion

            apiService.getVehiculos(estadoQuery).enqueue(object : Callback<VehiculoListResponse> {
                override fun onResponse(
                    call: Call<VehiculoListResponse>,
                    response: Response<VehiculoListResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val body = response.body()!!
                        if (body.success && body.data.isNotEmpty()) {
                            val sb = StringBuilder()
                            for (veh in body.data) {
                                sb.append("ID: ${veh.idVehiculo}\n")
                                sb.append("Marca: ${veh.marca}\n")
                                sb.append("Modelo: ${veh.modelo}\n")
                                sb.append("Año: ${veh.anio}\n")
                                sb.append("Estado: ${veh.estado}\n")
                                sb.append("─────────────────\n")
                            }
                            txtResultados.text = sb.toString()
                        } else {
                            txtResultados.text = "No se encontraron vehículos"
                        }
                    } else {
                        txtResultados.text = "Error en la respuesta del servidor"
                    }
                }

                override fun onFailure(call: Call<VehiculoListResponse>, t: Throwable) {
                    txtResultados.text = "Error de conexión: ${t.message}"
                    Toast.makeText(this@VehiculosWebActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
