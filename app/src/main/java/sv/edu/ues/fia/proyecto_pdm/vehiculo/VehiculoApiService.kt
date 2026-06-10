package sv.edu.ues.fia.proyecto_pdm.vehiculo

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VehiculoApiService {

    @GET("get_vehiculos.php")
    fun getVehiculos(@Query("estado") estado: String?): Call<VehiculoListResponse>
}

data class VehiculoResponse(
    @SerializedName("IdVehiculo") val idVehiculo: Int,
    @SerializedName("Marca") val marca: String,
    @SerializedName("Modelo") val modelo: String,
    @SerializedName("Anio") val anio: Int,
    @SerializedName("Estado") val estado: String,
    @SerializedName("IdUbicacion") val idUbicacion: Int?,
    @SerializedName("IdImportacion") val idImportacion: Int?
)

data class VehiculoListResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<VehiculoResponse>
)
