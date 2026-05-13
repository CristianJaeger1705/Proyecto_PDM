package sv.edu.ues.fia.proyecto_pdm

object DatabaseContract {
    object MedioTransporteEntry {
        const val TABLE_NAME = "Medio_Transporte"
        const val COLUMN_ID = "IdMedio"
        const val COLUMN_TIPO = "Tipo"
        const val COLUMN_CAPACIDAD = "CapacidadMax"
    }

    object UsuarioEntry {
        const val TABLE_NAME = "Usuario"
        const val COLUMN_USERNAME = "Username"
        const val COLUMN_PASSWORD = "Password"
    }

    object VehiculoEntry {
        const val TABLE_NAME = "Vehiculo"
        const val COLUMN_ID = "IdVehiculo"
        const val COLUMN_MARCA = "Marca"
        const val COLUMN_ESTADO = "Estado" // 0: Disponible, 1: Vendido
    }

    object VentaEntry {
        const val TABLE_NAME = "Venta"
        const val COLUMN_ID = "IdVenta"
        const val COLUMN_ID_VEHICULO = "IdVehiculo"
        const val COLUMN_PRECIO = "PrecioVenta"
        const val COLUMN_ID_IMPORTADOR = "NUIImportador"
        const val COLUMN_FECHA = "FechaVenta"
    }

    object ImportadorEntry {
        const val TABLE_NAME = "Importador"
        const val COLUMN_NUI = "NUI"
        const val COLUMN_NOMBRE = "Nombre"
    }

    object MovimientoEntry {
        const val TABLE_NAME = "Movimiento"
        const val COLUMN_ID = "IdMovimiento"
        const val COLUMN_ID_MEDIO = "IdMedio"
        const val COLUMN_TIPO = "TipoMovimiento"
        const val COLUMN_FECHA = "Fecha"
        const val COLUMN_HORA = "Hora"
        const val COLUMN_OBSERVACIONES = "Observaciones"
    }

    object MovimientoVehiculoEntry {
        const val TABLE_NAME = "Movimiento_Vehiculo"
        const val COLUMN_ID_MOVIMIENTO = "IdMovimiento"
        const val COLUMN_ID_VEHICULO = "IdVehiculo"
    }
}