package sv.edu.ues.fia.proyecto_pdm.importador

data class TelefonoResponse(
    val IdTelefono: Int,
    val Numero: String,
    val TipoTelefono: String?
)

data class ImportadorResponse(
    val NUI: String,
    val Nombre: String,
    val Apellido: String,
    val ApellidoCasada: String?,
    val Genero: String,
    val FechaNacimiento: String,
    val Direccion: String,
    val CorreoElectronico: String,
    val NUI_Responsable: String?,
    val Telefonos: List<TelefonoResponse>
)

data class ImportadoresListResponse(
    val success: Boolean,
    val data: List<ImportadorResponse>
)

data class ImportacionRequest(
    val NUIImportador: String,
    val CantidadVehiculos: Int,
    val FechaImportacion: String
)

data class ImportacionResponse(
    val success: Boolean,
    val IdImportacion: Int?,
    val mensaje: String
)