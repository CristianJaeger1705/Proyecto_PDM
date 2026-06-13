package sv.edu.ues.fia.proyecto_pdm.taller

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface TallerApiService {
    @GET("obtener_talleres.php")
    fun obtenerTalleres(): Call<TallerResponse>

    @FormUrlEncoded
    @POST("insertar_taller.php")
    fun insertarTaller(
        @Field("IdTaller") idTaller: Int,
        @Field("NombreTaller") nombreTaller: String,
        @Field("Direccion") direccion: String,
        @Field("Telefono") telefono: String,
        @Field("Autorizado") autorizado: String
    ): Call<TallerResponse>
}

data class TallerResponse(
    val success: Boolean,
    val mensaje: String?,
    val data: List<Taller>?
)
