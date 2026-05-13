package sv.edu.ues.fia.proyecto_pdm.ventas

data class Venta(
    val idVenta: Int,
    val idVehiculo: Int,
    val precioVenta: Double,
    val nuiImportador: String,
    val fechaVenta: String
)
