package sv.edu.ues.fia.proyecto_pdm.seccion

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.bodega.Bodega
import sv.edu.ues.fia.proyecto_pdm.bodega.BodegaHandler

class SeccionEliminarActivity : BaseActivity() {

    private lateinit var seccionHandler: SeccionHandler
    private lateinit var bodegaHandler: BodegaHandler
    
    private lateinit var spinnerBodegas: Spinner
    private lateinit var spinnerSecciones: Spinner
    private lateinit var btnEliminar: Button
    
    private lateinit var listaBodegas: List<Bodega>
    private lateinit var listaSecciones: List<Seccion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seccion_eliminar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        seccionHandler = SeccionHandler(this)
        bodegaHandler = BodegaHandler(this)

        spinnerBodegas = findViewById(R.id.spinnerEliminarBodegas)
        spinnerSecciones = findViewById(R.id.spinnerEliminarSecciones)
        btnEliminar = findViewById(R.id.btnEliminarSeccionFinal)

        cargarBodegas()

        spinnerBodegas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cargarSecciones(listaBodegas[position].idBodega)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnEliminar.setOnClickListener { confirmarEliminacion() }
    }

    private fun cargarBodegas() {
        listaBodegas = bodegaHandler.obtenerTodas()
        if (listaBodegas.isEmpty()) {
            Toast.makeText(this, "No hay bodegas", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaBodegas.map { it.nombreBodega })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBodegas.adapter = adapter
    }

    private fun cargarSecciones(idBodega: Int) {
        listaSecciones = seccionHandler.obtenerPorBodega(idBodega)
        if (listaSecciones.isEmpty()) {
            spinnerSecciones.adapter = null
            return
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaSecciones.map { "ID: ${it.idSeccion} - Nivel ${it.nivel}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSecciones.adapter = adapter
    }

    private fun confirmarEliminacion() {
        if (spinnerSecciones.selectedItem == null) {
            Toast.makeText(this, "Seleccione una sección", Toast.LENGTH_SHORT).show()
            return
        }

        val idSeccion = listaSecciones[spinnerSecciones.selectedItemPosition].idSeccion

        AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Está seguro de que desea eliminar la sección ID: $idSeccion? Esta acción no se puede deshacer.")
            .setPositiveButton("Sí, Eliminar") { _, _ ->
                ejecutarEliminacion(idSeccion)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun ejecutarEliminacion(id: Int) {
        val res = seccionHandler.eliminar(id)
        if (res > 0) {
            Toast.makeText(this, "Sección eliminada con éxito", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al eliminar la sección", Toast.LENGTH_SHORT).show()
        }
    }
}
