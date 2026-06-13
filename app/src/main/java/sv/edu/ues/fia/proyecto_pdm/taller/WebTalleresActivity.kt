package sv.edu.ues.fia.proyecto_pdm.taller

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sv.edu.ues.fia.proyecto_pdm.BaseActivity
import sv.edu.ues.fia.proyecto_pdm.R
import sv.edu.ues.fia.proyecto_pdm.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WebTalleresActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TallerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_talleres)

        recyclerView = findViewById(R.id.recyclerWebTalleres)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = TallerAdapter(emptyList()) { taller ->
            Toast.makeText(this, "Taller: ${taller.nombreTaller}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        cargarTalleresWeb()
    }

    private fun cargarTalleresWeb() {
        val apiService = RetrofitClient.instance.create(TallerApiService::class.java)
        apiService.obtenerTalleres().enqueue(object : Callback<TallerResponse> {
            override fun onResponse(call: Call<TallerResponse>, response: Response<TallerResponse>) {
                if (response.isSuccessful) {
                    val list = response.body()?.data ?: emptyList()
                    adapter.updateList(list)
                } else {
                    Toast.makeText(this@WebTalleresActivity, "Error en la respuesta", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TallerResponse>, t: Throwable) {
                Toast.makeText(this@WebTalleresActivity, "Fallo de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
