package sv.edu.ues.fia.proyecto_pdm.vehiculo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.RetrofitClient

class WebAgregarVehiculoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_agregar_vehiculo)

        val editId = findViewById<EditText>(R.id.editWebIdVehiculo)
        val editMarca = findViewById<EditText>(R.id.editWebMarca)
        val editModelo = findViewById<EditText>(R.id.editWebModelo)
        val editAnio = findViewById<EditText>(R.id.editWebAnio)
        val editUbicacion = findViewById<EditText>(R.id.editWebIdUbicacion)
        val editImportacion = findViewById<EditText>(R.id.editWebIdImportacion)
        val btnGuardar = findViewById<Button>(R.id.btnWebGuardarVehiculo)

        val apiService = RetrofitClient.instance.create(VehiculoApiService::class.java)

        btnGuardar.setOnClickListener {
            val id = editId.text.toString()
            val marca = editMarca.text.toString()
            val modelo = editModelo.text.toString()
            val anio = editAnio.text.toString()
            val ubicacion = editUbicacion.text.toString()
            val importacion = editImportacion.text.toString()

            if (id.isEmpty() || marca.isEmpty() || modelo.isEmpty() || anio.isEmpty() || ubicacion.isEmpty() || importacion.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnGuardar.isEnabled = false

            apiService.insertarVehiculo(
                id.toInt(),
                marca,
                modelo,
                anio.toInt(),
                "DISPONIBLE",
                ubicacion.toInt(),
                importacion.toInt()
            ).enqueue(object : Callback<VehiculoInsertResponse> {
                override fun onResponse(
                    call: Call<VehiculoInsertResponse>,
                    response: Response<VehiculoInsertResponse>
                ) {
                    btnGuardar.isEnabled = true
                    if (response.isSuccessful && response.body() != null) {
                        val res = response.body()!!
                        Toast.makeText(this@WebAgregarVehiculoActivity, res.mensaje, Toast.LENGTH_LONG).show()
                        if (res.success) {
                            finish() // Regresar si fue exitoso
                        }
                    } else {
                        Toast.makeText(this@WebAgregarVehiculoActivity, "Error en el servidor", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<VehiculoInsertResponse>, t: Throwable) {
                    btnGuardar.isEnabled = true
                    Toast.makeText(this@WebAgregarVehiculoActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
