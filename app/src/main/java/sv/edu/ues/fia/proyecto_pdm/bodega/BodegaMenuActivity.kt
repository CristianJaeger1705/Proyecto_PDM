package sv.edu.ues.fia.proyecto_pdm.bodega

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R

class BodegaMenuActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bodega_menu)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnInsertar = findViewById<Button>(R.id.btnMenuInsertar)
        val btnConsultar = findViewById<Button>(R.id.btnMenuConsultar)
        val btnActualizar = findViewById<Button>(R.id.btnMenuActualizar)
        val btnEliminar = findViewById<Button>(R.id.btnMenuEliminar)

        // Validar permisos (Prefix 06)
        if (!tienePermiso("061")) btnInsertar.visibility = View.GONE
        if (!tienePermiso("062")) btnActualizar.visibility = View.GONE
        if (!tienePermiso("063")) btnConsultar.visibility = View.GONE
        if (!tienePermiso("064")) btnEliminar.visibility = View.GONE

        btnInsertar.setOnClickListener {
            startActivity(Intent(this, BodegaInsertarActivity::class.java))
        }

        btnConsultar.setOnClickListener {
            startActivity(Intent(this, BodegaConsultarActivity::class.java))
        }

        btnActualizar.setOnClickListener {
            startActivity(Intent(this, BodegaActualizarActivity::class.java))
        }

        btnEliminar.setOnClickListener {
            startActivity(Intent(this, BodegaEliminarActivity::class.java))
        }

        findViewById<Button>(R.id.btnGestionSecciones).setOnClickListener {
            startActivity(Intent(this, sv.edu.ues.fia.proyecto_pdm.seccion.SeccionMenuActivity::class.java))
        }

        findViewById<Button>(R.id.btnGestionUbicaciones).setOnClickListener {
            startActivity(Intent(this, sv.edu.ues.fia.proyecto_pdm.ubicacion.UbicacionMenuActivity::class.java))
        }

        findViewById<Button>(R.id.btnBodegasWeb).setOnClickListener {
            startActivity(Intent(this, WebBodegasActivity::class.java))
        }

        findViewById<Button>(R.id.btnDisponibilidadWeb).setOnClickListener {
            startActivity(Intent(this, WebDisponibilidadActivity::class.java))
        }
    }
}
