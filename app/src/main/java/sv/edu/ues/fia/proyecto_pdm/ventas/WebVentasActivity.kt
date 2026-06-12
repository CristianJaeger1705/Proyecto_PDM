package sv.edu.ues.fia.proyecto_pdm.ventas

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.RetrofitClient

class WebVentasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_ventas)

        val editFiltroNui = findViewById<EditText>(R.id.editFiltroNui)
        val btnCargar = findViewById<Button>(R.id.btnCargarVentas)
        val txtResultados = findViewById<TextView>(R.id.txtResultadosVentas)

        val apiService = RetrofitClient.instance.create(VentaApiService::class.java)

        btnCargar.setOnClickListener {
            txtResultados.text = "Cargando..."

            val nui = editFiltroNui.text.toString().trim()
            val queryNui = if (nui.isEmpty()) null else nui

            apiService.getVentas(queryNui).enqueue(object : Callback<VentaListResponse> {
                override fun onResponse(
                    call: Call<VentaListResponse>,
                    response: Response<VentaListResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val body = response.body()!!
                        if (body.success && body.data.isNotEmpty()) {
                            val sb = StringBuilder()
                            for (venta in body.data) {
                                sb.append("ID Venta: ${venta.idVenta}\n")
                                sb.append("ID Vehículo: ${venta.idVehiculo}\n")
                                sb.append("Precio: $${venta.precioVenta}\n")
                                sb.append("Importador (NUI): ${venta.nuiImportador}\n")
                                sb.append("Fecha: ${venta.fechaVenta}\n")
                                sb.append("─────────────────\n")
                            }
                            txtResultados.text = sb.toString()
                        } else {
                            txtResultados.text = "No se encontraron ventas"
                        }
                    } else {
                        txtResultados.text = "Error en la respuesta del servidor"
                    }
                }

                override fun onFailure(call: Call<VentaListResponse>, t: Throwable) {
                    txtResultados.text = "Error de conexión: ${t.message}"
                    Toast.makeText(this@WebVentasActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
