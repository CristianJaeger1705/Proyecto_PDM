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
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.RetrofitClient

class WebImportacionActivity : AppCompatActivity() {

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
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantidad = cantidadStr.toIntOrNull()
            if (cantidad == null) {
                Toast.makeText(this, "La cantidad debe ser un número", Toast.LENGTH_SHORT).show()
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
                            txtResultado.text = "✅ ${body.mensaje}\nID generado: ${body.IdImportacion}"
                            editNUI.text.clear()
                            editCantidad.text.clear()
                            editFecha.text.clear()
                        } else {
                            txtResultado.text = "❌ ${body.mensaje}"
                        }
                    } else {
                        txtResultado.text = "Error en la respuesta del servidor"
                    }
                }

                override fun onFailure(call: Call<ImportacionResponse>, t: Throwable) {
                    txtResultado.text = "Error de conexión: ${t.message}"
                    Toast.makeText(
                        this@WebImportacionActivity,
                        "No se pudo conectar al servidor",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}