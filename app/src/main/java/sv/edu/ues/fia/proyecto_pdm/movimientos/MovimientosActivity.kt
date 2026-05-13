package sv.edu.ues.fia.proyecto_pdm.movimientos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.transporte.GestionTransporteActivity

class MovimientosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimientos)

        val btnOpcionMedios = findViewById<ImageButton>(R.id.btnOpcionMedios)
        val btnOpcionMovimientos = findViewById<ImageButton>(R.id.btnOpcionMovimientos)

        btnOpcionMedios.setOnClickListener {
            val intent = Intent(this, GestionTransporteActivity::class.java)
            startActivity(intent)
        }

        btnOpcionMovimientos.setOnClickListener {
            val intent = Intent(this, GestionMovimientosActivity::class.java)
            startActivity(intent)
        }
    }
}
