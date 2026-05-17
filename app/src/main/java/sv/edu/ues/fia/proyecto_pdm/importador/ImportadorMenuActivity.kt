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

class ImportadorMenuActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_importador_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnInsertar = findViewById<Button>(R.id.btnImportadorInsertar)
        val btnConsultar = findViewById<Button>(R.id.btnImportadorConsultar)
        val btnActualizar = findViewById<Button>(R.id.btnImportadorActualizar)
        val btnEliminar = findViewById<Button>(R.id.btnImportadorEliminar)

        // Validar permisos
        if (!tienePermiso("011")) btnInsertar.visibility = View.GONE
        if (!tienePermiso("012")) btnActualizar.visibility = View.GONE
        if (!tienePermiso("013")) btnConsultar.visibility = View.GONE
        if (!tienePermiso("014")) btnEliminar.visibility = View.GONE

        btnInsertar.setOnClickListener {
            startActivity(Intent(this, ImportadorInsertarActivity::class.java))
        }

        btnConsultar.setOnClickListener {
            startActivity(Intent(this, ImportadorConsultarActivity::class.java))
        }

        btnActualizar.setOnClickListener {
            startActivity(Intent(this, ImportadorActualizarActivity::class.java))
        }

        btnEliminar.setOnClickListener {
            startActivity(Intent(this, ImportadorEliminarActivity::class.java))
        }
        findViewById<Button>(R.id.btnTelefonosImportador).setOnClickListener {
            startActivity(Intent(this, TelefonoImportadorMenuActivity::class.java))
        }
    }
}