package sv.edu.ues.fia.proyecto_pdm.vehiculo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.Vehiculo
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler
import sv.edu.ues.fia.proyecto_pdm.importacion.ImportacionHandler

class VehiculoGestionActivity : BaseActivity() {

    private lateinit var editIdVehiculo: EditText
    private lateinit var editMarca: EditText
    private lateinit var editModelo: EditText
    private lateinit var editAnio: EditText
    private lateinit var editIdImportacion: EditText
    private lateinit var btnInsertar: Button
    private lateinit var btnConsultar: Button
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var btnIrAEstado: Button

    private lateinit var vehiculoHandler: VehiculoHandler
    private lateinit var importacionHandler: ImportacionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehiculo_gestion)

        vehiculoHandler = VehiculoHandler(this)
        importacionHandler = ImportacionHandler(this)

        editIdVehiculo = findViewById(R.id.editIdVehiculo)
        editMarca = findViewById(R.id.editMarca)
        editModelo = findViewById(R.id.editModelo)
        editAnio = findViewById(R.id.editAnio)
        editIdImportacion = findViewById(R.id.editIdImportacionVehiculo)

        btnInsertar = findViewById(R.id.btnInsertarVehiculo)
        btnConsultar = findViewById(R.id.btnConsultarVehiculo)
        btnActualizar = findViewById(R.id.btnActualizarVehiculo)
        btnEliminar = findViewById(R.id.btnEliminarVehiculo)
        btnLimpiar = findViewById(R.id.btnLimpiarVehiculo)
        btnIrAEstado = findViewById(R.id.btnIrAEstadoVehicular)

        // Validar permisos (Prefix 04)
        if (!tienePermiso("041")) btnInsertar.visibility = View.GONE
        if (!tienePermiso("042")) btnActualizar.visibility = View.GONE
        if (!tienePermiso("043")) btnConsultar.visibility = View.GONE
        if (!tienePermiso("044")) btnEliminar.visibility = View.GONE

        btnInsertar.setOnClickListener { insertar() }
        btnConsultar.setOnClickListener { consultar() }
        btnActualizar.setOnClickListener { actualizar() }
        btnEliminar.setOnClickListener { eliminar() }
        btnLimpiar.setOnClickListener { limpiar() }
        btnIrAEstado.setOnClickListener {
            val intent = android.content.Intent(this, sv.edu.ues.fia.proyecto_pdm.estadovehicular.EstadoVehicularGestionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun insertar() {
        val id = editIdVehiculo.text.toString()
        val marca = editMarca.text.toString()
        val modelo = editModelo.text.toString()
        val anio = editAnio.text.toString()
        val idImp = editIdImportacion.text.toString()

        if (id.isEmpty() || marca.isEmpty() || modelo.isEmpty() || anio.isEmpty() || idImp.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Validar si la importación existe
        val importacion = importacionHandler.consultar(idImp.toInt())
        if (importacion == null) {
            Toast.makeText(this, "La importación con ID $idImp no existe", Toast.LENGTH_SHORT).show()
            return
        }

        val vehiculo = Vehiculo(
            idVehiculo = id.toInt(),
            marca = marca,
            modelo = modelo,
            anio = anio.toInt(),
            idImportacion = idImp.toInt()
        )

        val resultado = vehiculoHandler.insertar(vehiculo)
        if (resultado != -1L) {
            Toast.makeText(this, "Vehículo insertado con éxito", Toast.LENGTH_SHORT).show()
            limpiar()
        } else {
            Toast.makeText(this, "Error al insertar. Posible duplicado o capacidad de importación alcanzada", Toast.LENGTH_LONG).show()
        }
    }

    private fun consultar() {
        val id = editIdVehiculo.text.toString()
        if (id.isEmpty()) {
            Toast.makeText(this, "Ingrese ID para consultar", Toast.LENGTH_SHORT).show()
            return
        }

        val vehiculo = vehiculoHandler.consultar(id.toInt())
        if (vehiculo != null) {
            editMarca.setText(vehiculo.marca)
            editModelo.setText(vehiculo.modelo)
            editAnio.setText(vehiculo.anio.toString())
            editIdImportacion.setText(vehiculo.idImportacion.toString())
            editIdVehiculo.isEnabled = false
        } else {
            Toast.makeText(this, "Vehículo no encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizar() {
        val id = editIdVehiculo.text.toString()
        val marca = editMarca.text.toString()
        val modelo = editModelo.text.toString()
        val anio = editAnio.text.toString()
        val idImp = editIdImportacion.text.toString()

        if (id.isEmpty() || marca.isEmpty() || modelo.isEmpty() || anio.isEmpty() || idImp.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val vehiculo = Vehiculo(
            idVehiculo = id.toInt(),
            marca = marca,
            modelo = modelo,
            anio = anio.toInt(),
            idImportacion = idImp.toInt()
        )

        val resultado = vehiculoHandler.actualizar(vehiculo)
        if (resultado > 0) {
            Toast.makeText(this, "Vehículo actualizado con éxito", Toast.LENGTH_SHORT).show()
            limpiar()
        } else {
            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminar() {
        val id = editIdVehiculo.text.toString()
        if (id.isEmpty()) {
            Toast.makeText(this, "Ingrese ID para eliminar", Toast.LENGTH_SHORT).show()
            return
        }

        val resultado = vehiculoHandler.eliminar(id.toInt())
        if (resultado > 0) {
            Toast.makeText(this, "Vehículo eliminado con éxito", Toast.LENGTH_SHORT).show()
            limpiar()
        } else {
            Toast.makeText(this, "Error al eliminar. Verifique el ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiar() {
        editIdVehiculo.setText("")
        editMarca.setText("")
        editModelo.setText("")
        editAnio.setText("")
        editIdImportacion.setText("")
        editIdVehiculo.isEnabled = true
    }
}
