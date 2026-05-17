package sv.edu.ues.fia.proyecto_pdm.ubicacion

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

class UbicacionMenuActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ubicacion_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnInsertar = findViewById<Button>(R.id.btnUbicacionInsertar)
        val btnConsultar = findViewById<Button>(R.id.btnUbicacionConsultar)
        val btnActualizar = findViewById<Button>(R.id.btnUbicacionActualizar)
        val btnEliminar = findViewById<Button>(R.id.btnUbicacionEliminar)

        // Validar permisos (Prefix 08)
        if (!tienePermiso("081")) btnInsertar.visibility = View.GONE
        if (!tienePermiso("082")) btnActualizar.visibility = View.GONE
        if (!tienePermiso("083")) btnConsultar.visibility = View.GONE
        if (!tienePermiso("084")) btnEliminar.visibility = View.GONE

        btnInsertar.setOnClickListener {
            startActivity(Intent(this, UbicacionInsertarActivity::class.java))
        }

        btnConsultar.setOnClickListener {
            startActivity(Intent(this, UbicacionConsultarActivity::class.java))
        }

        btnActualizar.setOnClickListener {
            startActivity(Intent(this, UbicacionActualizarActivity::class.java))
        }

        btnEliminar.setOnClickListener {
            startActivity(Intent(this, UbicacionEliminarActivity::class.java))
        }
    }
}
