package sv.edu.ues.fia.proyecto_pdm.seccion

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

class SeccionMenuActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seccion_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnInsertar = findViewById<Button>(R.id.btnSeccionInsertar)
        val btnConsultar = findViewById<Button>(R.id.btnSeccionConsultar)
        val btnActualizar = findViewById<Button>(R.id.btnSeccionActualizar)
        val btnEliminar = findViewById<Button>(R.id.btnSeccionEliminar)

        // Validar permisos (Prefix 07)
        if (!tienePermiso("071")) btnInsertar.visibility = View.GONE
        if (!tienePermiso("072")) btnActualizar.visibility = View.GONE
        if (!tienePermiso("073")) btnConsultar.visibility = View.GONE
        if (!tienePermiso("074")) btnEliminar.visibility = View.GONE

        btnInsertar.setOnClickListener {
            startActivity(Intent(this, SeccionInsertarActivity::class.java))
        }

        btnConsultar.setOnClickListener {
            startActivity(Intent(this, SeccionConsultarActivity::class.java))
        }

        btnActualizar.setOnClickListener {
            startActivity(Intent(this, SeccionActualizarActivity::class.java))
        }

        btnEliminar.setOnClickListener {
            startActivity(Intent(this, SeccionEliminarActivity::class.java))
        }
    }
}
