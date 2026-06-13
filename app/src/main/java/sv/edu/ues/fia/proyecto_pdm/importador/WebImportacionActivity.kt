package sv.edu.ues.fia.proyecto_pdm.importador

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.RetrofitClient

class WebImportacionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_importacion)

        val editNUI = findViewById<EditText>(R.id.editNUIImportador)
        val editCantidad = findViewById<EditText>(R.id.editCantidadVehiculos)
        val editFecha = findViewById<EditText>(R.id.editFechaImportacion)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrarImportacion)
        val txtResultado = findViewById<TextView>(R.id.txtResultadoImportacion)

        val apiService = RetrofitClient.instance.create(ImportadorApiService::class.java)

        btnRegistrar.setOnClickListener {
            val nui = editNUI.text.toString()
            val cantidadStr = editCantidad.text.toString()
            val fecha = editFecha.text.toString()

            if (nui.isEmpty() || cantidadStr.isEmpty() || fecha.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantidad = cantidadStr.toIntOrNull()
            if (cantidad == null) {
                Toast.makeText(this, "Debe ser un número", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = ImportacionRequest(nui, cantidad, fecha)

            apiService.postImportacion(request).enqueue(object : Callback<ImportacionResponse> {
                override fun onResponse(
                    call: Call<ImportacionResponse>,
                    response: Response<ImportacionResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val body = response.body()!!
                        if (body.success) {
                            txtResultado.text = "✅ ${body.mensaje}\nID: ${body.IdImportacion}"
                            editNUI.text.clear()
                            editCantidad.text.clear()
                            editFecha.text.clear()
                        } else {
                            txtResultado.text = "❌ ${body.mensaje}"
                        }
                    } else {
                        txtResultado.text = getString(R.string.msg_server_error_code, response.code())
                    }
                }

                override fun onFailure(call: Call<ImportacionResponse>, t: Throwable) {
                    txtResultado.text = getString(R.string.msg_connection_failure)
                }
            })
        }
    }
}