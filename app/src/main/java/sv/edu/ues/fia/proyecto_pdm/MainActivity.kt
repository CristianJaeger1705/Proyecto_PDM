package sv.edu.ues.fia.proyecto_pdm

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.movimientos.MovimientosActivity
import sv.edu.ues.fia.proyecto_pdm.ventas.GestionVentasActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnIrAMovimientosHub = findViewById<ImageButton>(R.id.btnIrAMovimientosHub)
        val btnIrAVentas = findViewById<ImageButton>(R.id.btnIrAVentas)
        val btnIrABodega = findViewById<ImageButton>(R.id.btnIrABodega)
        val btnIrAImportadores = findViewById<ImageButton>(R.id.btnIrAImportadores)

        btnIrAMovimientosHub.setOnClickListener {
            val intent = Intent(this, MovimientosActivity::class.java)
            startActivity(intent)
        }

        btnIrAVentas.setOnClickListener {
            val intent = Intent(this, GestionVentasActivity::class.java)
            startActivity(intent)
        }

        btnIrABodega.setOnClickListener {
            val intent = Intent(this, sv.edu.ues.fia.proyecto_pdm.bodega.BodegaMenuActivity::class.java)
            startActivity(intent)
        }
        btnIrAImportadores.setOnClickListener {
            startActivity(Intent(this, sv.edu.ues.fia.proyecto_pdm.importador.ImportadorMenuActivity::class.java))
        }
    }
}
