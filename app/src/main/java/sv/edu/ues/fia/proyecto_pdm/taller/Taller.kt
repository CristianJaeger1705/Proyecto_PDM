package sv.edu.ues.fia.proyecto_pdm.taller

import com.google.gson.annotations.SerializedName

data class Taller(
    @SerializedName("idTaller")
    val idTaller: Int,
    @SerializedName("nombreTaller")
    val nombreTaller: String?,
    @SerializedName("direccion")
    val direccion: String?,
    @SerializedName("telefono")
    val telefono: String?,
    @SerializedName("autorizado")
    val autorizado: String? // 'S' o 'N'
)
