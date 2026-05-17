package sv.edu.ues.fia.proyecto_pdm.importacion

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

class ImportacionMenuActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_importacion_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnInsertar = findViewById<Button>(R.id.btnImportacionInsertar)
        val btnConsultar = findViewById<Button>(R.id.btnImportacionConsultar)
        val btnActualizar = findViewById<Button>(R.id.btnImportacionActualizar)
        val btnEliminar = findViewById<Button>(R.id.btnImportacionEliminar)

        // Validar permisos (Prefix 12)
        if (!tienePermiso("121")) btnInsertar.visibility = View.GONE
        if (!tienePermiso("122")) btnActualizar.visibility = View.GONE
        if (!tienePermiso("123")) btnConsultar.visibility = View.GONE
        if (!tienePermiso("124")) btnEliminar.visibility = View.GONE

        btnInsertar.setOnClickListener {
            startActivity(Intent(this, ImportacionInsertarActivity::class.java))
        }

        btnConsultar.setOnClickListener {
            startActivity(Intent(this, ImportacionConsultarActivity::class.java))
        }

        btnActualizar.setOnClickListener {
            startActivity(Intent(this, ImportacionActualizarActivity::class.java))
        }

        btnEliminar.setOnClickListener {
            startActivity(Intent(this, ImportacionEliminarActivity::class.java))
        }
    }
}
