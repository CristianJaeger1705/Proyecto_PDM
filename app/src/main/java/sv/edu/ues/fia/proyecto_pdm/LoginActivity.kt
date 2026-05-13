package sv.edu.ues.fia.proyecto_pdm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sv.edu.ues.fia.proyecto_pdm.usuarios.UsuarioHandler
import sv.edu.ues.fia.proyecto_pdm.transporte.MedioTransporteHandler
import sv.edu.ues.fia.proyecto_pdm.transporte.MedioTransporte

class LoginActivity : AppCompatActivity() {

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

        btnLogin.setOnClickListener {
            val user = editUser.text.toString()
            val pass = editPass.text.toString()

            if (usuarioHandler.validarLogin(user, pass)) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }

        btnLlenarBD.setOnClickListener {
            llenarBaseDatos()
        }
    }

    private fun llenarBaseDatos() {
        // Insertar usuario de prueba
        usuarioHandler.insertarUsuario("admin", "123")
        
        // Insertar algunos medios de transporte
        medioHandler.insertar(MedioTransporte(1, "Carro", 1))
        medioHandler.insertar(MedioTransporte(2, "Grua", 1))
        medioHandler.insertar(MedioTransporte(3, "Coyote", 12))

        // Insertar algunos vehiculos de prueba
        vehiculoHandler.insertar(Vehiculo(1, "Toyota"))
        vehiculoHandler.insertar(Vehiculo(2, "Nissan"))
        vehiculoHandler.insertar(Vehiculo(3, "Honda"))

        // Insertar algunos importadores de prueba (NUI de 10 caracteres)
        importadorHandler.insertar(Importador(
            nui = "12345678-0",
            nombre = "Juan",
            apellido = "Pérez",
            apellidoCasada = null,
            genero = "M",
            fechaNacimiento = "1990-05-15",
            direccion = "Col. Miramonte, San Salvador",
            correoElectronico = "juan.perez@email.com",
            nuiResponsable = null
        ))
        importadorHandler.insertar(Importador(
            nui = "87654321-9",
            nombre = "María",
            apellido = "González",
            apellidoCasada = "de López",
            genero = "F",
            fechaNacimiento = "1985-03-22",
            direccion = "Res. Santa Elena, Santa Tecla",
            correoElectronico = "maria.gonzalez@email.com",
            nuiResponsable = null
        ))

        Toast.makeText(this, "BD Llena. Usuario: admin, Pass: 123", Toast.LENGTH_LONG).show()
    }
}
