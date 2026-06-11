package sv.edu.ues.fia.proyecto_pdm.bodega

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface BodegaApiService {

    @GET("get_bodegas.php")
    fun getBodegas(): Call<BodegaListResponse>

    @FormUrlEncoded
    @POST("post_bodegas.php")
    fun insertarBodega(
        @Field("IdBodega") idBodega: Int,
        @Field("NombreBodega") nombreBodega: String,
        @Field("Departamento") departamento: String,
        @Field("Direccion") direccion: String,
        @Field("CapacidadSecciones") capacidadSecciones: Int
    ): Call<BodegaWebResponse>

    @GET("get_bodegas_disponibilidad.php")
    fun getBodegasDisponibilidad(): Call<BodegaDisponibilidadListResponse>
}

data class BodegaWebResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("mensaje") val mensaje: String
)

data class BodegaListResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<BodegaData>
)

data class BodegaData(
    @SerializedName("IdBodega") val idBodega: Int,
    @SerializedName("NombreBodega") val nombreBodega: String,
    @SerializedName("Departamento") val departamento: String,
    @SerializedName("Direccion") val direccion: String,
    @SerializedName("CapacidadSecciones") val capacidadSecciones: Int
)

data class BodegaDisponibilidadListResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<BodegaDisponibilidadData>
)

data class BodegaDisponibilidadData(
    @SerializedName("IdBodega") val idBodega: Int,
    @SerializedName("NombreBodega") val nombreBodega: String,
    @SerializedName("secciones") val secciones: List<SeccionDisponibilidadData>
)

data class SeccionDisponibilidadData(
    @SerializedName("IdSeccion") val idSeccion: Int,
    @SerializedName("Nivel") val nivel: Int,
    @SerializedName("CapacidadMax") val capacidadMax: Int,
    @SerializedName("ocupados") val ocupados: Int,
    @SerializedName("disponibles") val disponibles: Int
)
