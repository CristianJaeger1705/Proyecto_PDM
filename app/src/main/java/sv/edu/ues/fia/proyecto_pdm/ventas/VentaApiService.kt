package sv.edu.ues.fia.proyecto_pdm.ventas

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface VentaApiService {

    @GET("get_ventas.php")
    fun getVentas(@Query("nuiImportador") nui: String? = null): Call<VentaListResponse>

    @POST("post_ventas.php")
    fun registrarVenta(@Body venta: Venta): Call<VentaResponse>
}

data class VentaResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("idVenta") val idVenta: Int? = null
)

data class VentaListResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<Venta>
)
