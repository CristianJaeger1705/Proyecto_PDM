package sv.edu.ues.fia.proyecto_pdm

data class Importador(
    val nui: String,
    val nombre: String,
    val apellido: String,
    val apellidoCasada: String? = null,
    val genero: String,
    val fechaNacimiento: String,
    val direccion: String,
    val correoElectronico: String,
    val nuiResponsable: String? = null
)
