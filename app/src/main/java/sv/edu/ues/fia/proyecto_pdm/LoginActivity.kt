package sv.edu.ues.fia.proyecto_pdm

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.usuarios.UsuarioHandler
import sv.edu.ues.fia.proyecto_pdm.transporte.MedioTransporteHandler
import sv.edu.ues.fia.proyecto_pdm.transporte.MedioTransporte
import sv.edu.ues.fia.proyecto_pdm.usuarios.OpcionCrudHandler
import sv.edu.ues.fia.proyecto_pdm.usuarios.AccesoUsuarioHandler
import sv.edu.ues.fia.proyecto_pdm.importador.TelefonoImportadorHandler
import sv.edu.ues.fia.proyecto_pdm.importador.TelefonoImportador
import sv.edu.ues.fia.proyecto_pdm.bodega.BodegaHandler
import sv.edu.ues.fia.proyecto_pdm.bodega.Bodega
import sv.edu.ues.fia.proyecto_pdm.seccion.SeccionHandler
import sv.edu.ues.fia.proyecto_pdm.seccion.Seccion
import sv.edu.ues.fia.proyecto_pdm.taller.TallerHandler
import sv.edu.ues.fia.proyecto_pdm.taller.Taller
import sv.edu.ues.fia.proyecto_pdm.ventas.VentaHandler
import sv.edu.ues.fia.proyecto_pdm.ventas.Venta
import sv.edu.ues.fia.proyecto_pdm.importacion.ImportacionHandler
import sv.edu.ues.fia.proyecto_pdm.importacion.Importacion
import java.util.Locale

class LoginActivity : BaseActivity() {

    private lateinit var usuarioHandler: UsuarioHandler
    private lateinit var medioHandler: MedioTransporteHandler
    private lateinit var vehiculoHandler: VehiculoHandler
    private lateinit var importadorHandler: ImportadorHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usuarioHandler = UsuarioHandler(this)
        medioHandler = MedioTransporteHandler(this)
        vehiculoHandler = VehiculoHandler(this)
        importadorHandler = ImportadorHandler(this)

        val editUser = findViewById<EditText>(R.id.editUser)
        val editPass = findViewById<EditText>(R.id.editPass)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnLlenarBD = findViewById<Button>(R.id.btnLlenarBD)
        val btnChangeLang = findViewById<Button>(R.id.btnChangeLang)

        btnLogin.setOnClickListener {
            val user = editUser.text.toString()
            val pass = editPass.text.toString()

            if (usuarioHandler.validarLogin(user, pass)) {
                // Guardar usuario en SharedPreferences
                val sharedPref = getSharedPreferences("nombre_usuario", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("username", user)
                    apply()
                }

                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT).show()
            }
        }

        btnLlenarBD.setOnClickListener {
            llenarBDGpo06()
        }

        btnChangeLang.setOnClickListener {
            toggleLanguage()
        }
    }

    private fun toggleLanguage() {
        val currentLocale = resources.configuration.locales.get(0).language
        val newLang = if (currentLocale == "es") "en" else "es"

        LocaleHelper.setLocale(this, newLang)

        // Restart activity to apply changes
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun llenarBDGpo06() {
        // 1. Usuarios y Permisos
        usuarioHandler.insertarUsuario("admin", "123")
        usuarioHandler.insertarUsuario("consultor", "con01")

        val entities = listOf(
            "Importador", "Telefono", "Venta", "Vehiculo", "Medio Transporte",
            "Bodega", "Seccion", "Ubicacion", "Taller", "Reparacion",
            "Movimiento", "Importacion", "Estado Vehicular", "Gasto Adicional"
        )

        val opcionHandler = OpcionCrudHandler(this)
        for ((index, entity) in entities.withIndex()) {
            val prefix = (index + 1).toString().padStart(2, '0')
            opcionHandler.insertar("${prefix}0", "Menu $entity", 0)
            opcionHandler.insertar("${prefix}1", "Agregar $entity", 1)
            opcionHandler.insertar("${prefix}2", "Editar $entity", 2)
            opcionHandler.insertar("${prefix}3", "Consultar $entity", 4)
            opcionHandler.insertar("${prefix}4", "Eliminar $entity", 3)
        }

        val accesoHandler = AccesoUsuarioHandler(this)
        for (i in 1..entities.size) {
            val prefix = i.toString().padStart(2, '0')
            // Admin: Todo
            for (j in 0..4) accesoHandler.insertar("$prefix$j", "admin")
            // Consultor: Menu y Consultar
            accesoHandler.insertar("${prefix}0", "consultor")
            accesoHandler.insertar("${prefix}3", "consultor")
        }

        // 2. Medios de Transporte (Gpo06 Capacidades)
        medioHandler.insertar(MedioTransporte(1, "Auto", 1))
        medioHandler.insertar(MedioTransporte(2, "Grua", 1))
        medioHandler.insertar(MedioTransporte(3, "Tacuazina", 12))

        // 3. Importadores (Gpo06 Campos completos)
        importadorHandler.insertar(Importador(
            nui = "IMP0000001",
            nombre = "Juan Carlos",
            apellido = "Pérez",
            apellidoCasada = null,
            genero = "M",
            fechaNacimiento = "1980-01-01",
            direccion = "San Salvador, El Salvador",
            correoElectronico = "juan@mail.com",
            nuiResponsable = null
        ))
        importadorHandler.insertar(Importador(
            nui = "IMP0000002",
            nombre = "Maria Elena",
            apellido = "Lopez",
            apellidoCasada = "de Perez",
            genero = "F",
            fechaNacimiento = "1985-05-15",
            direccion = "Santa Tecla, La Libertad",
            correoElectronico = "maria@mail.com",
            nuiResponsable = "IMP0000001"
        ))

        // 4. Teléfonos
        val telHandler = TelefonoImportadorHandler(this)
        telHandler.insertar(TelefonoImportador(null, "IMP0000001", "2222-3333", "CASA"))
        telHandler.insertar(TelefonoImportador(null, "IMP0000001", "7777-8888", "CELULAR"))

        // 5. Bodegas
        val bodegaHandler = BodegaHandler(this)
        val seccionHandler = SeccionHandler(this)
        val nombresBodegas = listOf("Central", "Occidente", "Oriente", "Norte", "Sur", "Puerto")
        for (i in 1..6) {
            val idB = bodegaHandler.insertar(Bodega().apply {
                nombreBodega = "Bodega ${nombresBodegas[i-1]}"
                departamento = "Departamento $i"
                direccion = "Direccion Bodega $i"
                capacidadSecciones = 3
            }).toInt()
            
            // Niveles
            for (nivel in 1..3) {
                seccionHandler.insertar(Seccion(0, idB, nivel, 50))
            }
        }

        // 6. Importaciones y Vehículos
        val impHandler = ImportacionHandler(this)
        impHandler.insertar(Importacion(1, "IMP0000001", 5, "2024-05-01"))
        
        vehiculoHandler.insertar(Vehiculo(1, "Toyota", "Corolla", 2021, "DISPONIBLE", null, 1))
        vehiculoHandler.insertar(Vehiculo(2, "Nissan", "Sentra", 2023, "DISPONIBLE", null, 1))
        vehiculoHandler.insertar(Vehiculo(3, "Honda", "Civic", 2024, "DISPONIBLE", null, 1))

        // 7. Talleres y Reparaciones
        val tallerHandler = TallerHandler(this)
        tallerHandler.insertar(Taller(0, "Taller El Rayo (Autorizado)", "San Salvador", "2255-8899", "S"))
        
        // 8. Ventas
        val ventaHandler = VentaHandler(this)
        ventaHandler.registrarVenta(Venta(1, 2, 15000.0, "IMP0000002", "2024-06-01"))

        Toast.makeText(this, "Datos Gpo06 cargados.", Toast.LENGTH_LONG).show()
    }
}
