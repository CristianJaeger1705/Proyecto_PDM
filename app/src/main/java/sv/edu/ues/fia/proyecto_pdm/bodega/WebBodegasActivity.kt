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

class WebBodegasActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_web_bodegas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnCargar = findViewById<Button>(R.id.btnCargarBodegasWeb)
        val txtResultados = findViewById<TextView>(R.id.txtResultadosBodegasWeb)

        val apiService = RetrofitClient.instance.create(BodegaApiService::class.java)

        btnCargar.setOnClickListener {
            txtResultados.text = getString(R.string.btn_load) + "..."

            apiService.getBodegas().enqueue(object : Callback<BodegaListResponse> {
                override fun onResponse(call: Call<BodegaListResponse>, response: Response<BodegaListResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val res = response.body()!!
                        if (res.success && res.data.isNotEmpty()) {
                            val sb = StringBuilder()
                            res.data.forEach { b ->
                                sb.append("ID: ${b.idBodega}\n")
                                sb.append("Nombre: ${b.nombreBodega}\n")
                                sb.append("Depto: ${b.departamento}\n")
                                sb.append("Dir: ${b.direccion}\n")
                                sb.append("Capacidad: ${b.capacidadSecciones}\n")
                                sb.append("─────────────────\n")
                            }
                            txtResultados.text = sb.toString()
                        } else {
                            txtResultados.text = getString(R.string.msg_server_connected_empty)
                        }
                    } else {
                        val code = response.code()
                        txtResultados.text = getString(R.string.msg_server_error_code, code)
                    }
                }

                override fun onFailure(call: Call<BodegaListResponse>, t: Throwable) {
                    txtResultados.text = getString(R.string.msg_connection_failure)
                }
            })
        }
    }
}
