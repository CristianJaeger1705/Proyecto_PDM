package sv.edu.ues.fia.proyecto_pdm.importador

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

class TelefonoImportadorMenuActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_telefono_importador_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnInsertar = findViewById<Button>(R.id.btnTelefonoInsertar)
        val btnConsultar = findViewById<Button>(R.id.btnTelefonoConsultar)
        val btnActualizar = findViewById<Button>(R.id.btnTelefonoActualizar)
        val btnEliminar = findViewById<Button>(R.id.btnTelefonoEliminar)

        // Validar permisos (Prefix 02)
        if (!tienePermiso("021")) btnInsertar.visibility = View.GONE
        if (!tienePermiso("022")) btnActualizar.visibility = View.GONE
        if (!tienePermiso("023")) btnConsultar.visibility = View.GONE
        if (!tienePermiso("024")) btnEliminar.visibility = View.GONE

        btnInsertar.setOnClickListener {
            startActivity(Intent(this, TelefonoImportadorInsertarActivity::class.java))
        }
        btnConsultar.setOnClickListener {
            startActivity(Intent(this, TelefonoImportadorConsultarActivity::class.java))
        }
        btnActualizar.setOnClickListener {
            startActivity(Intent(this, TelefonoImportadorActualizarActivity::class.java))
        }
        btnEliminar.setOnClickListener {
            startActivity(Intent(this, TelefonoImportadorEliminarActivity::class.java))
        }
    }
}