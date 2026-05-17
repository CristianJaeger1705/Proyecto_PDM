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
import sv.edu.ues.fia.proyecto_pdm.vehiculo.VehiculoGestionActivity
import sv.edu.ues.fia.proyecto_pdm.gastos.GastoAdicionalGestionActivity
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : BaseActivity() {

    private lateinit var vehiculoHandler: VehiculoHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vehiculoHandler = VehiculoHandler(this)

        val btnIrAMovimientosHub = findViewById<ImageButton>(R.id.btnIrAMovimientosHub)
        val btnIrAVentas = findViewById<ImageButton>(R.id.btnIrAVentas)
        val btnIrABodega = findViewById<ImageButton>(R.id.btnIrABodega)
        val btnIrATaller = findViewById<ImageButton>(R.id.btnIrATaller)
        val btnIrAReparaciones = findViewById<ImageButton>(R.id.btnIrAReparaciones)
        val btnIrAImportadores = findViewById<ImageButton>(R.id.btnIrAImportadores)
        val btnIrAImportaciones = findViewById<ImageButton>(R.id.btnIrAImportaciones)
        val btnIrAVehiculo = findViewById<ImageButton>(R.id.btnIrAVehiculo)
        val btnIrAGastos = findViewById<ImageButton>(R.id.btnIrAGastos)
        

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

        btnIrAVehiculo.setOnClickListener {
            startActivity(Intent(this, VehiculoGestionActivity::class.java))
        }

        btnIrAGastos.setOnClickListener {
            startActivity(Intent(this, GastoAdicionalGestionActivity::class.java))
        }

        actualizarDashboard()
    }

    override fun onResume() {
        super.onResume()
        actualizarDashboard()
    }

    private fun actualizarDashboard() {
        val sharedPref = getSharedPreferences("nombre_usuario", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "") ?: ""
        val layoutDashboard = findViewById<LinearLayout>(R.id.layoutDashboard)

        if (username == "admin") {
            layoutDashboard.visibility = View.VISIBLE
            val conteos = vehiculoHandler.obtenerConteoPorEstado()
            
            findViewById<TextView>(R.id.txtCountDisponible).text = (conteos["DISPONIBLE"] ?: 0).toString()
            findViewById<TextView>(R.id.txtCountReparacion).text = (conteos["EN_REPARACION"] ?: 0).toString()
            findViewById<TextView>(R.id.txtCountVendido).text = (conteos["VENDIDO"] ?: 0).toString()
        } else {
            layoutDashboard.visibility = View.GONE
        }
    }
}
