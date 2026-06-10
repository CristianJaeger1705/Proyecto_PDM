package sv.edu.ues.fia.proyecto_pdm.importador

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.RetrofitClient

class WebImportadoresActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_importadores)

        val btnCargar = findViewById<Button>(R.id.btnCargarImportadores)
        val txtResultados = findViewById<TextView>(R.id.txtResultados)

        val apiService = RetrofitClient.instance.create(ImportadorApiService::class.java)

        btnCargar.setOnClickListener {
            txtResultados.text = "Cargando..."

            apiService.getImportadores().enqueue(object : Callback<ImportadoresListResponse> {
                override fun onResponse(
                    call: Call<ImportadoresListResponse>,
                    response: Response<ImportadoresListResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val body = response.body()!!
                        if (body.success && body.data.isNotEmpty()) {
                            val sb = StringBuilder()
                            for (imp in body.data) {
                                sb.append("NUI: ${imp.NUI}\n")
                                sb.append("Nombre: ${imp.Nombre} ${imp.Apellido}\n")
                                sb.append("Correo: ${imp.CorreoElectronico}\n")
                                sb.append("Dirección: ${imp.Direccion}\n")
                                if (imp.Telefonos.isNotEmpty()) {
                                    sb.append("Teléfonos:\n")
                                    for (tel in imp.Telefonos) {
                                        sb.append("  - ${tel.Numero} (${tel.TipoTelefono})\n")
                                    }
                                }
                                sb.append("─────────────────\n")
                            }
                            txtResultados.text = sb.toString()
                        } else {
                            txtResultados.text = "No hay importadores registrados en el servidor"
                        }
                    } else {
                        txtResultados.text = "Error en la respuesta del servidor"
                    }
                }

                override fun onFailure(call: Call<ImportadoresListResponse>, t: Throwable) {
                    txtResultados.text = "Error de conexión: ${t.message}"
                    Toast.makeText(
                        this@WebImportadoresActivity,
                        "No se pudo conectar al servidor",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}