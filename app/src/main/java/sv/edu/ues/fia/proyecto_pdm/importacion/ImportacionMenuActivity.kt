package sv.edu.ues.fia.proyecto_pdm.importacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sv.edu.ues.fia.proyecto_pdm.R

class ImportacionMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_importacion_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btnImportacionInsertar).setOnClickListener {
            startActivity(Intent(this, ImportacionInsertarActivity::class.java))
        }

        findViewById<Button>(R.id.btnImportacionConsultar).setOnClickListener {
            startActivity(Intent(this, ImportacionConsultarActivity::class.java))
        }

        findViewById<Button>(R.id.btnImportacionActualizar).setOnClickListener {
            startActivity(Intent(this, ImportacionActualizarActivity::class.java))
        }

        findViewById<Button>(R.id.btnImportacionEliminar).setOnClickListener {
            startActivity(Intent(this, ImportacionEliminarActivity::class.java))
        }
    }
}
