package sv.edu.ues.fia.proyecto_pdm.taller

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R

class TallerInsertarActivity : AppCompatActivity() {

    private lateinit var handler: TallerHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taller_insertar)

        handler = TallerHandler(this)

        // val editId = findViewById<EditText>(R.id.editId) // Eliminado
        val editNombre = findViewById<EditText>(R.id.editNombre)
        val editDireccion = findViewById<EditText>(R.id.editDireccion)
        val editTelefono = findViewById<EditText>(R.id.editTelefono)
        val checkAutorizado = findViewById<CheckBox>(R.id.checkAutorizado)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnGuardarWeb = findViewById<Button>(R.id.btnGuardarWeb)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)

        btnGuardar.setOnClickListener {
            val nombre = editNombre.text.toString()
            val direccion = editDireccion.text.toString()
            val telefono = editTelefono.text.toString()
            val autorizado = if (checkAutorizado.isChecked) "S" else "N"

            if (nombre.isNotEmpty()) {
                val taller = Taller(
                    idTaller = 0, // El ID será generado por la DB
                    nombreTaller = nombre,
                    direccion = direccion,
                    telefono = telefono,
                    autorizado = autorizado,
                )
                
                val resultado = handler.insertar(taller)
                if (resultado != -1L) {
                    Toast.makeText(this, getString(R.string.msg_workshop_saved, resultado), Toast.LENGTH_SHORT).show()
                    limpiar()
                } else {
                    Toast.makeText(this, getString(R.string.insert_error), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.msg_workshop_name_required), Toast.LENGTH_SHORT).show()
            }
        }

        btnGuardarWeb.setOnClickListener {
            val nombre = editNombre.text.toString()
            val direccion = editDireccion.text.toString()
            val telefono = editTelefono.text.toString()
            val autorizado = if (checkAutorizado.isChecked) "S" else "N"

            if (nombre.isNotEmpty()) {
                val apiService = sv.edu.ues.fia.proyecto_pdm.RetrofitClient.instance.create(TallerApiService::class.java)
                apiService.insertarTaller(0, nombre, direccion, telefono, autorizado).enqueue(object : retrofit2.Callback<TallerResponse> {
                    override fun onResponse(call: retrofit2.Call<TallerResponse>, response: retrofit2.Response<TallerResponse>) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            Toast.makeText(this@TallerInsertarActivity, "Taller guardado en WEB con éxito", Toast.LENGTH_SHORT).show()
                            limpiar()
                        } else {
                            Toast.makeText(this@TallerInsertarActivity, "Error en WEB: ${response.body()?.mensaje}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<TallerResponse>, t: Throwable) {
                        Toast.makeText(this@TallerInsertarActivity, "Fallo de conexión WEB: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
            }
        }

        btnLimpiar.setOnClickListener {
            limpiar()
        }
    }

    private fun limpiar() {
        // findViewById<EditText>(R.id.editId).text.clear() // Eliminado
        findViewById<EditText>(R.id.editNombre).text.clear()
        findViewById<EditText>(R.id.editDireccion).text.clear()
        findViewById<EditText>(R.id.editTelefono).text.clear()
        findViewById<CheckBox>(R.id.checkAutorizado).isChecked = false
    }
}
