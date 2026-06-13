package sv.edu.ues.fia.proyecto_pdm.taller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R

class TallerGestionActivity : BaseActivity() {

    private lateinit var handler: TallerHandler
    private var tallerActual: Taller? = null
    private lateinit var adapter: TallerAdapter
    private lateinit var talleres: List<Taller>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taller_gestion)

        handler = TallerHandler(this)

        val spinnerTalleres = findViewById<Spinner>(R.id.spinnerGestionTallerId)
        val editNombre = findViewById<EditText>(R.id.editNombre)
        val editDireccion = findViewById<EditText>(R.id.editDireccion)
        val editTelefono = findViewById<EditText>(R.id.editTelefono)
        val checkAutorizado = findViewById<CheckBox>(R.id.checkAutorizado)

        val btnIrACrear = findViewById<Button>(R.id.btnIrACrear)
        val btnIrATalleresWeb = findViewById<Button>(R.id.btnIrATalleresWeb)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)
        val recyclerTalleres = findViewById<RecyclerView>(R.id.recyclerTalleres)

        // Validar permisos (Prefix 09)
        if (!tienePermiso("091")) btnIrACrear.visibility = View.GONE
        if (!tienePermiso("092")) btnActualizar.visibility = View.GONE
        if (!tienePermiso("093")) btnBuscar.visibility = View.GONE
        if (!tienePermiso("094")) btnEliminar.visibility = View.GONE

        // Configurar RecyclerView
        adapter = TallerAdapter(emptyList()) { taller ->
            llenarCampos(taller)
            tallerActual = taller
            val index = talleres.indexOfFirst { it.idTaller == taller.idTaller }
            if (index != -1) spinnerTalleres.setSelection(index)
        }
        recyclerTalleres.layoutManager = LinearLayoutManager(this)
        recyclerTalleres.adapter = adapter

        actualizarLista()
        cargarTalleres(spinnerTalleres)

        btnIrACrear.setOnClickListener {
            val intent = Intent(this, TallerInsertarActivity::class.java)
            startActivity(intent)
        }

        btnIrATalleresWeb.setOnClickListener {
            val intent = Intent(this, WebTalleresActivity::class.java)
            startActivity(intent)
        }

        btnBuscar.setOnClickListener {
            val pos = spinnerTalleres.selectedItemPosition
            if (pos != -1) {
                tallerActual = talleres[pos]
                llenarCampos(tallerActual!!)
                Toast.makeText(this, "Registro encontrado", Toast.LENGTH_SHORT).show()
            }
        }

        btnActualizar.setOnClickListener {
            if (tallerActual != null) {
                val nombre = editNombre.text.toString()
                val direccion = editDireccion.text.toString()
                val telefono = editTelefono.text.toString()
                val autorizado = if (checkAutorizado.isChecked) "S" else "N"

                if (nombre.isNotEmpty()) {
                    val tallerEditado = Taller(
                        idTaller = tallerActual!!.idTaller,
                        nombreTaller = nombre,
                        direccion = direccion,
                        telefono = telefono,
                        autorizado = autorizado,
                    )
                    val filasAfectadas = handler.actualizar(tallerEditado)
                    if (filasAfectadas > 0) {
                        Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
                        actualizarLista()
                        cargarTalleres(spinnerTalleres)
                    } else {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Primero busque un registro", Toast.LENGTH_SHORT).show()
            }
        }

        btnEliminar.setOnClickListener {
            if (tallerActual != null) {
                val id = tallerActual!!.idTaller
                val filasEliminadas = handler.eliminar(id)
                if (filasEliminadas > 0) {
                    Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                    tallerActual = null
                    actualizarLista()
                    cargarTalleres(spinnerTalleres)
                } else {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Primero busque un registro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarLista()
    }

    private fun limpiarCampos() {
        findViewById<EditText>(R.id.editNombre).text.clear()
        findViewById<EditText>(R.id.editDireccion).text.clear()
        findViewById<EditText>(R.id.editTelefono).text.clear()
        findViewById<CheckBox>(R.id.checkAutorizado).isChecked = false
    }

    private fun llenarCampos(taller: Taller) {
        findViewById<EditText>(R.id.editNombre).setText(taller.nombreTaller)
        findViewById<EditText>(R.id.editDireccion).setText(taller.direccion)
        findViewById<EditText>(R.id.editTelefono).setText(taller.telefono)
        findViewById<CheckBox>(R.id.checkAutorizado).isChecked = taller.autorizado?.trim()?.uppercase() == "S"
    }

    private fun actualizarLista() {
        val talleresList = handler.obtenerTodos()
        adapter.updateList(talleresList)
    }

    private fun cargarTalleres(spinner: Spinner) {
        talleres = handler.obtenerTodos()
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, talleres.map { "${it.idTaller} - ${it.nombreTaller}" })
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner
    }
}
