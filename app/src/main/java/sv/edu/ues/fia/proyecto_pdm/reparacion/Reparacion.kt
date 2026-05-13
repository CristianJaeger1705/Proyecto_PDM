package sv.edu.ues.fia.proyecto_pdm.reparacion

data class Reparacion(
    val idReparacion: Int,
    val idTaller: Int,
    val idVehiculo: Int,
    val fechaEntrada: String,
    val fechaSalida: String?,
    val descripcionTrabajo: String,
    val aptoParaVenta: String, // 'S' o 'N'
    val costo: Double,
)
