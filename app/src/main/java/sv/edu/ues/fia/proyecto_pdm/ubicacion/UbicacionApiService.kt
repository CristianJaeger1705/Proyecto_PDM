package sv.edu.ues.fia.proyecto_pdm.ubicacion

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface UbicacionApiService {

    @PUT("put_ubicacion_vehiculo.php")
    fun actualizarUbicacionWeb(@Body request: UbicacionWebRequest): Call<UbicacionWebResponse>
}

data class UbicacionWebRequest(
    @SerializedName("IdVehiculo") val idVehiculo: Int,
    @SerializedName("IdSeccion") val idSeccion: Int,
    @SerializedName("FechaAsignacion") val fechaAsignacion: String
)

data class UbicacionWebResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("mensaje") val mensaje: String
)
