package sv.edu.ues.fia.proyecto_pdm.importador

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ImportadorApiService {

    @GET("get_importadores.php")
    fun getImportadores(): Call<ImportadoresListResponse>

    @POST("post_importaciones.php")
    fun postImportacion(@Body importacion: ImportacionRequest): Call<ImportacionResponse>
}