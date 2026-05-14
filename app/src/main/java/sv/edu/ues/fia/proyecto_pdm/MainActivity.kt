package sv.edu.ues.fia.proyecto_pdm

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.movimientos.MovimientosActivity
import sv.edu.ues.fia.proyecto_pdm.ventas.GestionVentasActivity
import sv.edu.ues.fia.proyecto_pdm.bodega.BodegaMenuActivity
import sv.edu.ues.fia.proyecto_pdm.taller.TallerGestionActivity
import sv.edu.ues.fia.proyecto_pdm.reparacion.ReparacionGestionActivity
import sv.edu.ues.fia.proyecto_pdm.importacion.ImportacionMenuActivity



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnIrAMovimientosHub = findViewById<ImageButton>(R.id.btnIrAMovimientosHub)
        val btnIrAVentas = findViewById<ImageButton>(R.id.btnIrAVentas)
        val btnIrABodega = findViewById<ImageButton>(R.id.btnIrABodega)
        val btnIrATaller = findViewById<ImageButton>(R.id.btnIrATaller)
        val btnIrAReparaciones = findViewById<ImageButton>(R.id.btnIrAReparaciones)
        val btnIrAImportadores = findViewById<ImageButton>(R.id.btnIrAImportadores)
        val btnIrAImportaciones = findViewById<ImageButton>(R.id.btnIrAImportaciones)
        

        btnIrAMovimientosHub.setOnClickListener {
            val intent = Intent(this, MovimientosActivity::class.java)
            startActivity(intent)
        }

        btnIrAVentas.setOnClickListener {
            val intent = Intent(this, GestionVentasActivity::class.java)
            startActivity(intent)
        }

        btnIrABodega.setOnClickListener {
            val intent = Intent(this, BodegaMenuActivity::class.java)
            startActivity(intent)
        }

        btnIrATaller.setOnClickListener {
            val intent = Intent(this, TallerGestionActivity::class.java)
            startActivity(intent)
        }

        btnIrAReparaciones.setOnClickListener {
            val intent = Intent(this, ReparacionGestionActivity::class.java)
            startActivity(intent)
        }
        btnIrAImportadores.setOnClickListener {
            startActivity(Intent(this, sv.edu.ues.fia.proyecto_pdm.importador.ImportadorMenuActivity::class.java))
        }

        btnIrAImportaciones.setOnClickListener {
            startActivity(Intent(this, ImportacionMenuActivity::class.java))
        }
        
    }
}
