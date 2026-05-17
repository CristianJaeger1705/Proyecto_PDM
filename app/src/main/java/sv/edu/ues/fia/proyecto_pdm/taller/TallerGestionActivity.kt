package sv.edu.ues.fia.proyecto_pdm.taller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taller_gestion)

        handler = TallerHandler(this)

        val editBusquedaId = findViewById<EditText>(R.id.editBusquedaId)
        val editNombre = findViewById<EditText>(R.id.editNombre)
        val editDireccion = findViewById<EditText>(R.id.editDireccion)
        val editTelefono = findViewById<EditText>(R.id.editTelefono)
        val editAutorizado = findViewById<EditText>(R.id.editAutorizado)

        val btnIrACrear = findViewById<Button>(R.id.btnIrACrear)
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
            editBusquedaId.setText(taller.idTaller.toString())
        }
        recyclerTalleres.layoutManager = LinearLayoutManager(this)
        recyclerTalleres.adapter = adapter

        actualizarLista()

        btnIrACrear.setOnClickListener {
            val intent = Intent(this, TallerInsertarActivity::class.java)
            startActivity(intent)
        }

        btnBuscar.setOnClickListener {
            val idStr = editBusquedaId.text.toString()
            if (idStr.isNotEmpty()) {
                val id = idStr.toInt()
                tallerActual = handler.buscar(id)
                
                if (tallerActual != null) {
                    editNombre.setText(tallerActual?.nombreTaller)
                    editDireccion.setText(tallerActual?.direccion)
                    editTelefono.setText(tallerActual?.telefono)
                    editAutorizado.setText(tallerActual?.autorizado)
                    Toast.makeText(this, "Registro encontrado", Toast.LENGTH_SHORT).show()
                } else {
                    limpiarCampos()
                    Toast.makeText(this, "No se encontró el ID: $id", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Ingrese un ID para buscar", Toast.LENGTH_SHORT).show()
            }
        }

        btnActualizar.setOnClickListener {
            if (tallerActual != null) {
                val nombre = editNombre.text.toString()
                val direccion = editDireccion.text.toString()
                val telefono = editTelefono.text.toString()
                val autorizado = editAutorizado.text.toString()

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
                    editBusquedaId.text.clear()
                    tallerActual = null
                    actualizarLista()
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
        findViewById<EditText>(R.id.editAutorizado).text.clear()
    }

    private fun llenarCampos(taller: Taller) {
        findViewById<EditText>(R.id.editNombre).setText(taller.nombreTaller)
        findViewById<EditText>(R.id.editDireccion).setText(taller.direccion)
        findViewById<EditText>(R.id.editTelefono).setText(taller.telefono)
        findViewById<EditText>(R.id.editAutorizado).setText(taller.autorizado)
    }

    private fun actualizarLista() {
        val talleres = handler.obtenerTodos()
        adapter.updateList(talleres)
    }
}
