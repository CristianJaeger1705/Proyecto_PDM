package sv.edu.ues.fia.proyecto_pdm.movimientos

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MovimientoApiService {

    @POST("post_movimientos.php")
    fun postMovimiento(@Body movimiento: MovimientoRequest): Call<MovimientoResponse>

    @GET("get_historial_vehiculo.php")
    fun getHistorialVehiculo(@Query("idVehiculo") idVehiculo: Int): Call<HistorialVehiculoResponse>
}
