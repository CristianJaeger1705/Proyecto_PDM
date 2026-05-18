package sv.edu.ues.fia.proyecto_pdm.movimientos

data class Movimiento(
    val idMovimiento: Int,
    val idMedio: Int,
    val tipoMovimiento: String,
    val fecha: String,
    val hora: String,
    val observaciones: String,
    val autorizado: Int = 0 // 0: No, 1: Sí
)
