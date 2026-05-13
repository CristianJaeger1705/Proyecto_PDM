package sv.edu.ues.fia.proyecto_pdm.taller

data class Taller(
    val idTaller: Int,
    val nombreTaller: String,
    val direccion: String,
    val telefono: String,
    val autorizado: String // 'S' o 'N'
)
