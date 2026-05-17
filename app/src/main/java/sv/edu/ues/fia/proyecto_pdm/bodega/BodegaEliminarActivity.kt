package sv.edu.ues.fia.proyecto_pdm.bodega

import android.os.Bundle
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

class BodegaEliminarActivity : BaseActivity() {

    private lateinit var helper: BodegaHandler
    private lateinit var spinnerBodegas: Spinner
    private lateinit var btnEliminar: Button
    private lateinit var listaBodegas: List<Bodega>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bodega_eliminar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        helper = BodegaHandler(this)
        spinnerBodegas = findViewById(R.id.spinnerEliminarBodegas)
        btnEliminar = findViewById(R.id.btnEliminarBodegaFinal)

        cargarBodegas()

        btnEliminar.setOnClickListener { confirmarEliminacion() }
    }

    private fun cargarBodegas() {
        listaBodegas = helper.obtenerTodas()
        if (listaBodegas.isEmpty()) {
            Toast.makeText(this, "No hay bodegas registradas", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaBodegas.map { it.nombreBodega })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBodegas.adapter = adapter
    }

    private fun confirmarEliminacion() {
        if (spinnerBodegas.selectedItem == null) return

        val bodega = listaBodegas[spinnerBodegas.selectedItemPosition]

        AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Está seguro de que desea eliminar la bodega '${bodega.nombreBodega}'? Esto eliminará también todas sus secciones y ubicaciones.")
            .setPositiveButton("Sí, Eliminar") { _, _ ->
                ejecutarEliminacion(bodega.idBodega)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun ejecutarEliminacion(id: Int) {
        val res = helper.eliminar(id)
        if (res > 0) {
            Toast.makeText(this, "Bodega eliminada con éxito", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al eliminar la bodega", Toast.LENGTH_SHORT).show()
        }
    }
}
