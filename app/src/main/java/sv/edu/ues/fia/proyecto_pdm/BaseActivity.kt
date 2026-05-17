package sv.edu.ues.fia.proyecto_pdm

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.usuarios.AccesoUsuarioHandler

open class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    fun tienePermiso(idOpcion: String): Boolean {
        val sharedPref = getSharedPreferences("nombre_usuario", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "") ?: ""
        if (username == "admin") return true // Admin siempre tiene permiso
        
        val accesoHandler = AccesoUsuarioHandler(this)
        return accesoHandler.tieneAcceso(username, idOpcion)
    }
}
