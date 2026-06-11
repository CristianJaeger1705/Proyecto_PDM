package sv.edu.ues.fia.proyecto_pdm.bodega

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.RetrofitClient

class WebDisponibilidadActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_web_bodegas) // Reutilizamos el layout de consulta web
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnCargar = findViewById<Button>(R.id.btnCargarBodegasWeb)
        val txtResultados = findViewById<TextView>(R.id.txtResultadosBodegasWeb)

        val apiService = RetrofitClient.instance.create(BodegaApiService::class.java)

        btnCargar.setOnClickListener {
            txtResultados.text = "Calculando disponibilidad en tiempo real..."

            apiService.getBodegasDisponibilidad().enqueue(object : Callback<BodegaDisponibilidadListResponse> {
                override fun onResponse(call: Call<BodegaDisponibilidadListResponse>, response: Response<BodegaDisponibilidadListResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val res = response.body()!!
                        if (res.data.isEmpty()) {
                            txtResultados.text = "Conectado al servidor: No hay bodegas registradas actualmente."
                            return
                        }
                        val sb = StringBuilder()
                        res.data.forEach { bodega ->
                            sb.append("🏢 BODEGA: ${bodega.nombreBodega}\n")
                            if (bodega.secciones.isEmpty()) {
                                sb.append("  (Esta bodega aún no tiene secciones registradas)\n")
                            }
                            bodega.secciones.forEach { seccion ->
                                sb.append("  • Sec ${seccion.idSeccion} (Nivel ${seccion.nivel}):\n")
                                sb.append("    Capacidad: ${seccion.capacidadMax} | Ocupados: ${seccion.ocupados}\n")
                                sb.append("    👉 Disponibles: ${seccion.disponibles}\n")
                            }
                            sb.append("─────────────────\n")
                        }
                        txtResultados.text = sb.toString()
                    } else {
                        val code = response.code()
                        txtResultados.text = "Error $code: El servidor no pudo procesar la solicitud. Verifique que el archivo PHP exista y la base de datos esté activa."
                    }
                }

                override fun onFailure(call: Call<BodegaDisponibilidadListResponse>, t: Throwable) {
                    txtResultados.text = "Fallo de conexión"
                }
            })
        }
    }
}
