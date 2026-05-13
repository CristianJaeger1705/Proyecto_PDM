package sv.edu.ues.fia.proyecto_pdm.transporte

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R

class InsertarMedioTransporteActivity : AppCompatActivity() {

    private lateinit var handler: MedioTransporteHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_medio_transporte)

        handler = MedioTransporteHandler(this)

        val editId = findViewById<EditText>(R.id.editNuevoId)
        val editTipo = findViewById<EditText>(R.id.editNuevoTipo)
        val editCapacidad = findViewById<EditText>(R.id.editNuevaCapacidad)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            val idStr = editId.text.toString()
            val tipo = editTipo.text.toString()
            val capacidadStr = editCapacidad.text.toString()

            if (idStr.isNotEmpty() && tipo.isNotEmpty() && capacidadStr.isNotEmpty()) {
                val idIngresado = idStr.toInt()
                val capacidad = capacidadStr.toInt()
                val nuevoMedio = MedioTransporte(idMedio = idIngresado, tipo = tipo, capacidadMax = capacidad)
                
                val idResult = handler.insertar(nuevoMedio)
                if (idResult != -1L) {
                    Toast.makeText(this, "Insertado con éxito. ID: $idResult", Toast.LENGTH_SHORT).show()
                    finish() // Regresar a la pantalla anterior
                } else {
                    Toast.makeText(this, "Error al insertar (posible ID duplicado)", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
