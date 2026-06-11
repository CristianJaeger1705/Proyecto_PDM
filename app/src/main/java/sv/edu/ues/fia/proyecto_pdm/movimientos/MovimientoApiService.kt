package sv.edu.ues.fia.proyecto_pdm.movimientos

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MovimientoApiService {

    @POST("post_movimientos.php")
    fun postMovimiento(@Body movimiento: MovimientoRequest): Call<MovimientoResponse>
}
