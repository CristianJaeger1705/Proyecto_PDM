package sv.edu.ues.fia.proyecto_pdm.movimientos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.transporte.MedioTransporteHandler
import sv.edu.ues.fia.proyecto_pdm.transporte.MedioTransporte
import java.util.Calendar

class InsertarMovimientoActivity : AppCompatActivity() {

    private lateinit var movHandler: MovimientoHandler
    private lateinit var medioHandler: MedioTransporteHandler
    private lateinit var medios: List<MedioTransporte>
    private val tiposMovimiento = arrayOf("ENTRADA", "SALIDA")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_movimiento)

        movHandler = MovimientoHandler(this)
        medioHandler = MedioTransporteHandler(this)

        val editId = findViewById<EditText>(R.id.editNewMovId)
        val spinnerMedios = findViewById<Spinner>(R.id.spinnerMedios)
        val spinnerTipo = findViewById<Spinner>(R.id.spinnerNewMovTipo)
        val editFecha = findViewById<EditText>(R.id.editNewMovFecha)
        val editHora = findViewById<EditText>(R.id.editNewMovHora)
        val editObs = findViewById<EditText>(R.id.editNewMovObs)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarNuevoMov)

        // Cargar Medios en el Spinner
        medios = medioHandler.obtenerTodos()
        val adapterMedios = ArrayAdapter(this, android.R.layout.simple_spinner_item, medios.map { it.tipo })
        adapterMedios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMedios.adapter = adapterMedios

        // Cargar Tipos de Movimiento (ENTRADA/SALIDA)
        val adapterTipos = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposMovimiento)
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipo.adapter = adapterTipos

        // Configurar DatePicker
        editFecha.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                editFecha.setText("$day/${month + 1}/$year")
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Configurar TimePicker
        editHora.setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute ->
                editHora.setText(String.format("%02d:%02d", hour, minute))
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        btnGuardar.setOnClickListener {
            val id = editId.text.toString().toIntOrNull()
            val posMedio = spinnerMedios.selectedItemPosition
            val tipo = spinnerTipo.selectedItem.toString()
            val fecha = editFecha.text.toString()
            val hora = editHora.text.toString()
            val obs = editObs.text.toString()

            if (id != null && posMedio != -1) {
                val idMedio = medios[posMedio].idMedio ?: -1
                val nuevo = Movimiento(id, idMedio, tipo, fecha, hora, obs)
                val res = movHandler.insertar(nuevo)
                if (res != -1L) {
                    Toast.makeText(this, getString(R.string.mov_save_success), Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, getString(R.string.mov_save_error), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.required_fields), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
