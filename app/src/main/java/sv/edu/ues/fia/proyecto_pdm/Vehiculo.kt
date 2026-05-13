package sv.edu.ues.fia.proyecto_pdm

data class Vehiculo(
    val idVehiculo: Int,
    val marca: String,
    val estado: String = "DISPONIBLE" // DISPONIBLE o VENDIDO
)
