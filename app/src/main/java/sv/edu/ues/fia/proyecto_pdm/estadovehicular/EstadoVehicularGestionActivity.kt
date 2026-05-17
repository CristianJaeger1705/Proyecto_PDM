package sv.edu.ues.fia.proyecto_pdm.estadovehicular

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.Vehiculo
import sv.edu.ues.fia.proyecto_pdm.VehiculoHandler
import java.io.ByteArrayOutputStream
import java.util.Calendar

class EstadoVehicularGestionActivity : BaseActivity() {

    private lateinit var spinnerVehiculo: Spinner
    private lateinit var spinnerHistorial: Spinner
    private lateinit var editDescripcion: EditText
    private lateinit var editFecha: EditText
    private lateinit var imgVehiculo: ImageView
    private lateinit var btnSeleccionarImagen: Button
    private lateinit var btnInsertar: Button
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnLimpiar: Button

    private lateinit var handler: EstadoVehicularHandler
    private lateinit var vehiculoHandler: VehiculoHandler
    
    private var listaVehiculos: List<Vehiculo> = emptyList()
    private var listaEstadosActuales: List<EstadoVehicular> = emptyList()
    private var estadoSeleccionado: EstadoVehicular? = null

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri -> imgVehiculo.setImageURI(uri) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estado_vehicular_gestion)

        handler = EstadoVehicularHandler(this)
        vehiculoHandler = VehiculoHandler(this)

        spinnerVehiculo = findViewById(R.id.spinnerVehiculoEstado)
        spinnerHistorial = findViewById(R.id.spinnerHistorialEstados)
        editDescripcion = findViewById(R.id.editDescripcionEstado)
        editFecha = findViewById(R.id.editFechaEstado)
        imgVehiculo = findViewById(R.id.imgVehiculoEstado)
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen)
        btnInsertar = findViewById(R.id.btnInsertarEstado)
        btnActualizar = findViewById(R.id.btnActualizarEstado)
        btnEliminar = findViewById(R.id.btnEliminarEstado)
        btnLimpiar = findViewById(R.id.btnLimpiarEstado)

        // Validar permisos (Prefix 13)
        if (!tienePermiso("131")) btnInsertar.visibility = View.GONE
        if (!tienePermiso("132")) btnActualizar.visibility = View.GONE
        // 133 es consultar (el spinner de historial)
        if (!tienePermiso("133")) spinnerHistorial.visibility = View.GONE
        if (!tienePermiso("134")) btnEliminar.visibility = View.GONE

        cargarVehiculos()

        editFecha.setOnClickListener {
            mostrarDatePicker()
        }

        spinnerVehiculo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cargarHistorial(listaVehiculos[position].idVehiculo!!)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerHistorial.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) { // Opción "NUEVO REGISTRO"
                    limpiarCampos()
                    estadoSeleccionado = null
                } else {
                    val resumen = listaEstadosActuales[position - 1]
                    // Consultamos el registro completo (con imagen) solo cuando se selecciona
                    val estadoCompleto = handler.consultar(resumen.idEstado!!)
                    if (estadoCompleto != null) {
                        cargarDatosEstado(estadoCompleto)
                        estadoSeleccionado = estadoCompleto
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnSeleccionarImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncher.launch(intent)
        }

        btnInsertar.setOnClickListener { insertar() }
        btnActualizar.setOnClickListener { actualizar() }
        btnEliminar.setOnClickListener { eliminar() }
        btnLimpiar.setOnClickListener {
            spinnerHistorial.setSelection(0)
            limpiarCampos()
        }
    }

    private fun mostrarDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Formato YYYY-MM-DD para consistencia y ordenamiento en BD
            val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            editFecha.setText(formattedDate)
        }, year, month, day)

        dpd.show()
    }

    private fun cargarVehiculos() {
        listaVehiculos = vehiculoHandler.obtenerTodos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaVehiculos.map { "${it.idVehiculo} - ${it.marca}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerVehiculo.adapter = adapter
    }

    private fun cargarHistorial(idVehiculo: Int) {
        listaEstadosActuales = handler.obtenerEstadosPorVehiculo(idVehiculo)
        val opciones = mutableListOf("--- NUEVO REGISTRO ---")
        opciones.addAll(listaEstadosActuales.map { it.fecha ?: "Sin fecha" })
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHistorial.adapter = adapter
    }

    private fun cargarDatosEstado(estado: EstadoVehicular) {
        editDescripcion.setText(estado.descripcion)
        editFecha.setText(estado.fecha)
        if (estado.imagen != null && estado.imagen!!.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeByteArray(estado.imagen, 0, estado.imagen!!.size)
            imgVehiculo.setImageBitmap(bitmap)
        } else {
            imgVehiculo.setImageDrawable(null)
        }
    }

    private fun insertar() {
        if (spinnerHistorial.selectedItemPosition != 0) {
            Toast.makeText(this, "Seleccione 'NUEVO REGISTRO' para insertar", Toast.LENGTH_SHORT).show()
            return
        }

        val descripcion = editDescripcion.text.toString()
        val fecha = editFecha.text.toString()
        val posVeh = spinnerVehiculo.selectedItemPosition

        if (descripcion.isEmpty() || fecha.isEmpty() || posVeh == -1) {
            Toast.makeText(this, "Complete descripción y fecha", Toast.LENGTH_SHORT).show()
            return
        }

        val estado = EstadoVehicular(
            idVehiculo = listaVehiculos[posVeh].idVehiculo,
            descripcion = descripcion,
            fecha = fecha,
            imagen = imageViewToByteArray(imgVehiculo)
        )

        val res = handler.insertar(estado)
        if (res != -1L) {
            Toast.makeText(this, "Estado guardado correctamente", Toast.LENGTH_SHORT).show()
            cargarHistorial(listaVehiculos[posVeh].idVehiculo!!)
        }
    }

    private fun actualizar() {
        if (estadoSeleccionado == null) {
            Toast.makeText(this, "Seleccione un estado del historial para actualizar", Toast.LENGTH_SHORT).show()
            return
        }

        estadoSeleccionado?.apply {
            descripcion = editDescripcion.text.toString()
            fecha = editFecha.text.toString()
            imagen = imageViewToByteArray(imgVehiculo)
        }

        val res = handler.actualizar(estadoSeleccionado!!)
        if (res > 0) {
            Toast.makeText(this, "Actualizado con éxito", Toast.LENGTH_SHORT).show()
            cargarHistorial(listaVehiculos[spinnerVehiculo.selectedItemPosition].idVehiculo!!)
        }
    }

    private fun eliminar() {
        if (estadoSeleccionado == null) {
            Toast.makeText(this, "Seleccione un estado del historial para eliminar", Toast.LENGTH_SHORT).show()
            return
        }

        val res = handler.eliminar(estadoSeleccionado!!.idEstado!!)
        if (res > 0) {
            Toast.makeText(this, "Eliminado con éxito", Toast.LENGTH_SHORT).show()
            cargarHistorial(listaVehiculos[spinnerVehiculo.selectedItemPosition].idVehiculo!!)
        }
    }

    private fun limpiarCampos() {
        editDescripcion.text.clear()
        editFecha.text.clear()
        imgVehiculo.setImageDrawable(null)
    }

    private fun imageViewToByteArray(imageView: ImageView): ByteArray? {
        val drawable = imageView.drawable ?: return null
        return try {
            var bitmap = (drawable as BitmapDrawable).bitmap
            
            // Redimensionar si es muy grande para evitar SQLiteBlobTooBigException
            val maxSize = 800
            if (bitmap.width > maxSize || bitmap.height > maxSize) {
                val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
                var width = maxSize
                var height = maxSize
                if (ratio > 1) {
                    height = (maxSize / ratio).toInt()
                } else {
                    width = (maxSize * ratio).toInt()
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
            }

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
            stream.toByteArray()
        } catch (e: Exception) { null }
    }
}
