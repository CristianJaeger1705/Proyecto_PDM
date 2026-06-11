package sv.edu.ues.fia.proyecto_pdm.bodega

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R

class BodegaConsultarActivity : BaseActivity() {

    private lateinit var helper: BodegaHandler
    private lateinit var spinnerBodegas: Spinner
    private lateinit var textResultado: TextView
    private lateinit var btnConsultar: Button
    private lateinit var btnSubirWeb: Button
    private lateinit var listaBodegas: List<Bodega>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bodega_consultar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        helper = BodegaHandler(this)
        spinnerBodegas = findViewById(R.id.spinnerConsultarBodegas)
        textResultado = findViewById(R.id.textResultadoBodega)
        btnConsultar = findViewById(R.id.btnConsultarBodegaDetalle)
        btnSubirWeb = findViewById(R.id.btnEnviarBodegaWeb)

        cargarBodegas()

        btnConsultar.setOnClickListener { mostrarInformacion() }
        btnSubirWeb.setOnClickListener { subirBodegaWeb() }
    }

    private fun cargarBodegas() {
        listaBodegas = helper.obtenerTodas()
        if (listaBodegas.isEmpty()) {
            Toast.makeText(this, "No hay bodegas registradas", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaBodegas.map { it.nombreBodega })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBodegas.adapter = adapter
    }

    private fun mostrarInformacion() {
        val bodega = listaBodegas[spinnerBodegas.selectedItemPosition]
        val conteoVehiculos = helper.obtenerConteoVehiculos(bodega.idBodega)
        
        val res = StringBuilder()
        res.append("DETALLE DE BODEGA:\n\n")
        res.append("🆔 ID: ${bodega.idBodega}\n")
        res.append("🏢 Nombre: ${bodega.nombreBodega}\n")
        res.append("📍 Departamento: ${bodega.departamento}\n")
        res.append("🏠 Dirección: ${bodega.direccion}\n")
        res.append("📦 Capacidad Secciones: ${bodega.capacidadSecciones}\n")
        res.append("\n📊 INVENTARIO ACTUAL:\n")
        res.append("🚗 Vehículos almacenados: $conteoVehiculos")
        
        textResultado.text = res.toString()
        btnSubirWeb.visibility = View.VISIBLE
    }

    private fun subirBodegaWeb() {
        val bodega = listaBodegas[spinnerBodegas.selectedItemPosition]
        val apiService = sv.edu.ues.fia.proyecto_pdm.RetrofitClient.instance.create(BodegaApiService::class.java)

        btnSubirWeb.isEnabled = false
        apiService.insertarBodega(
            bodega.idBodega,
            bodega.nombreBodega,
            bodega.departamento,
            bodega.direccion,
            bodega.capacidadSecciones
        ).enqueue(object : retrofit2.Callback<BodegaWebResponse> {
            override fun onResponse(call: retrofit2.Call<BodegaWebResponse>, response: retrofit2.Response<BodegaWebResponse>) {
                btnSubirWeb.isEnabled = true
                if (response.isSuccessful && response.body() != null) {
                    val res = response.body()!!
                    Toast.makeText(this@BodegaConsultarActivity, res.mensaje, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@BodegaConsultarActivity, "Error en el servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<BodegaWebResponse>, t: Throwable) {
                btnSubirWeb.isEnabled = true
                Toast.makeText(this@BodegaConsultarActivity, "Fallo conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
