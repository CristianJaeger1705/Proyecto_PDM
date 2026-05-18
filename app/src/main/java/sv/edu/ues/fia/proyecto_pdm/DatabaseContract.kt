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
        const val COLUMN_MODELO = "Modelo"
        const val COLUMN_ANIO = "Anio"
        const val COLUMN_ESTADO = "Estado" // DISPONIBLE o VENDIDO
        const val COLUMN_ID_UBICACION = "IdUbicacion"
        const val COLUMN_ID_IMPORTACION = "IdImportacion"
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
        const val COLUMN_APELLIDO = "Apellido"
        const val COLUMN_APELLIDO_CASADA = "ApellidoCasada"
        const val COLUMN_GENERO = "Genero"
        const val COLUMN_FECHA_NACIMIENTO = "FechaNacimiento"
        const val COLUMN_DIRECCION = "Direccion"
        const val COLUMN_CORREO = "CorreoElectronico"
        const val COLUMN_NUI_RESPONSABLE = "NUI_Responsable"
    }

    object MovimientoEntry {
        const val TABLE_NAME = "Movimiento"
        const val COLUMN_ID = "IdMovimiento"
        const val COLUMN_ID_MEDIO = "IdMedio"
        const val COLUMN_TIPO = "TipoMovimiento"
        const val COLUMN_FECHA = "Fecha"
        const val COLUMN_HORA = "Hora"
        const val COLUMN_OBSERVACIONES = "Observaciones"
        const val COLUMN_AUTORIZADO = "Autorizado" // 0: No, 1: Sí
    }

    object MovimientoVehiculoEntry {
        const val TABLE_NAME = "Movimiento_Vehiculo"
        const val COLUMN_ID_MOVIMIENTO = "IdMovimiento"
        const val COLUMN_ID_VEHICULO = "IdVehiculo"
    }

    object BodegaEntry {
        const val TABLE_NAME = "Bodega"
        const val COLUMN_ID = "IdBodega"
        const val COLUMN_NOMBRE = "NombreBodega"
        const val COLUMN_DEPARTAMENTO = "Departamento"
        const val COLUMN_DIRECCION = "Direccion"
        const val COLUMN_CAPACIDAD = "CapacidadSecciones"
    }

    object SeccionEntry {
        const val TABLE_NAME = "Seccion"
        const val COLUMN_ID = "IdSeccion"
        const val COLUMN_ID_BODEGA = "IdBodega"
        const val COLUMN_NIVEL = "Nivel"
        const val COLUMN_CAPACIDAD = "CapacidadMax"
    }

    object UbicacionEntry {
        const val TABLE_NAME = "Ubicacion_Vehiculo"
        const val COLUMN_ID = "IdUbicacion"
        const val COLUMN_ID_SECCION = "IdSeccion"
        const val COLUMN_ID_VEHICULO = "IdVehiculo"
        const val COLUMN_FECHA = "FechaAsignacion"
        const val COLUMN_ACTIVA = "Activa"
    }

    object TallerEntry {
        const val TABLE_NAME = "Taller"
        const val COLUMN_ID = "IdTaller"
        const val COLUMN_NOMBRE = "NombreTaller"
        const val COLUMN_DIRECCION = "Direccion"
        const val COLUMN_TELEFONO = "Telefono"
        const val COLUMN_AUTORIZADO = "Autorizado"
    }

    object ReparacionEntry {
        const val TABLE_NAME = "Reparacion"
        const val COLUMN_ID = "IdReparacion"
        const val COLUMN_ID_TALLER = "IdTaller"
        const val COLUMN_ID_VEHICULO = "IdVehiculo"
        const val COLUMN_FECHA_ENTRADA = "FechaEntrada"
        const val COLUMN_FECHA_SALIDA = "FechaSalida"
        const val COLUMN_DESCRIPCION = "DescripcionTrabajo"
        const val COLUMN_APTO = "AptoParaVenta"
        const val COLUMN_COSTO = "Costo"
    }

    object TelefonoImportadorEntry {
        const val TABLE_NAME = "Telefono_Importador"
        const val COLUMN_ID = "IdTelefono"
        const val COLUMN_NUI = "NUI"
        const val COLUMN_NUMERO = "Numero"
        const val COLUMN_TIPO = "TipoTelefono"
    }

    object ImportacionEntry {
        const val TABLE_NAME = "Importacion"
        const val COLUMN_ID = "IdImportacion"
        const val COLUMN_ID_IMPORTADOR = "NUIImportador"
        const val COLUMN_CANTIDAD_VEHICULOS = "CantidadVehiculos"
        const val COLUMN_FECHA = "FechaImportacion"
    }

    object EstadoVehicularEntry {
        const val TABLE_NAME = "Estado_Vehicular"
        const val COLUMN_ID = "IdEstado"
        const val COLUMN_ID_VEHICULO = "IdVehiculo"
        const val COLUMN_DESCRIPCION = "Descripcion"
        const val COLUMN_IMAGEN = "Imagen"
        const val COLUMN_FECHA = "FechaEstado"
    }

    object GastoAdicionalEntry {
        const val TABLE_NAME = "Gasto_Adicional"
        const val COLUMN_ID = "IdGasto"
        const val COLUMN_ID_VEHICULO = "IdVehiculo"
        const val COLUMN_CONCEPTO = "Concepto"
        const val COLUMN_MONTO = "Monto"
        const val COLUMN_FECHA = "FechaGasto"
    }
    object OpcionCrudEntry {
        const val TABLE_NAME = "Opcion_Crud"
        const val COLUMN_ID = "IdOpcion"
        const val COLUMN_DESCRIPCION = "DesOpcion"
        const val COLUMN_NUM_CRUD = "NumCrud"
    }

    object AccesoUsuarioEntry {
        const val TABLE_NAME = "Acceso_Usuario"
        const val COLUMN_ID_OPCION = "IdOpcion"
        const val COLUMN_ID_USUARIO = "IdUsuario"
    }
}