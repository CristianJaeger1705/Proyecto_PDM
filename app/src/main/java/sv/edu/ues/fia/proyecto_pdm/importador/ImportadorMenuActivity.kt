package sv.edu.ues.fia.proyecto_pdm.importador

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sv.edu.ues.fia.proyecto_pdm.R

class ImportadorMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_importador_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btnImportadorInsertar).setOnClickListener {
            startActivity(Intent(this, ImportadorInsertarActivity::class.java))
        }

        findViewById<Button>(R.id.btnImportadorConsultar).setOnClickListener {
            startActivity(Intent(this, ImportadorConsultarActivity::class.java))
        }

        findViewById<Button>(R.id.btnImportadorActualizar).setOnClickListener {
            startActivity(Intent(this, ImportadorActualizarActivity::class.java))
        }

        findViewById<Button>(R.id.btnImportadorEliminar).setOnClickListener {
            startActivity(Intent(this, ImportadorEliminarActivity::class.java))
        }
    }
}