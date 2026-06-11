package sv.edu.ues.fia.proyecto_pdm.movimientos

import com.google.gson.annotations.SerializedName

data class MovimientoRequest(
    @SerializedName("IdMedio") val idMedio: Int,
    @SerializedName("TipoMovimiento") val tipoMovimiento: String,
    @SerializedName("Fecha") val fecha: String,
    @SerializedName("Hora") val hora: String,
    @SerializedName("Observaciones") val observaciones: String,
    @SerializedName("vehiculos") val vehiculos: List<Int>
)

data class MovimientoResponse(
    val success: Boolean,
    @SerializedName("IdMovimiento") val idMovimiento: Int?,
    val mensaje: String
)
