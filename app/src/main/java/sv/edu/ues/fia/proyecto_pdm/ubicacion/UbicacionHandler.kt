package sv.edu.ues.fia.proyecto_pdm.ubicacion

import android.content.ContentValues
import android.content.Context
import sv.edu.ues.fia.proyecto_pdm.DatabaseContract
import sv.edu.ues.fia.proyecto_pdm.DatabaseHelper
import java.time.LocalDate

class UbicacionHandler(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun insertar(ubicacion: Ubicacion_vehiculo): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.UbicacionEntry.COLUMN_ID_SECCION, ubicacion.idSeccion)
            put(DatabaseContract.UbicacionEntry.COLUMN_ID_VEHICULO, ubicacion.idVehiculo)
            put(DatabaseContract.UbicacionEntry.COLUMN_FECHA, ubicacion.fechaAsignacion?.toString())
            put(DatabaseContract.UbicacionEntry.COLUMN_ACTIVA, if (ubicacion.activa == true) 1 else 0)
        }
        return db.insert(DatabaseContract.UbicacionEntry.TABLE_NAME, null, values)
    }

    fun consultar(id: Int): Ubicacion_vehiculo? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.UbicacionEntry.TABLE_NAME,
            null,
            "${DatabaseContract.UbicacionEntry.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        var ubicacion: Ubicacion_vehiculo? = null
        if (cursor.moveToFirst()) {
            val fechaStr = cursor.getString(3)
            val fecha = if (fechaStr != null) LocalDate.parse(fechaStr) else null
            ubicacion = Ubicacion_vehiculo(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getInt(2),
                fecha,
                cursor.getInt(4) == 1
            )
        }
        cursor.close()
        return ubicacion
    }

    fun actualizar(ubicacion: Ubicacion_vehiculo): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.UbicacionEntry.COLUMN_ID_SECCION, ubicacion.idSeccion)
            put(DatabaseContract.UbicacionEntry.COLUMN_ID_VEHICULO, ubicacion.idVehiculo)
            put(DatabaseContract.UbicacionEntry.COLUMN_FECHA, ubicacion.fechaAsignacion?.toString())
            put(DatabaseContract.UbicacionEntry.COLUMN_ACTIVA, if (ubicacion.activa == true) 1 else 0)
        }
        return db.update(
            DatabaseContract.UbicacionEntry.TABLE_NAME,
            values,
            "${DatabaseContract.UbicacionEntry.COLUMN_ID} = ?",
            arrayOf(ubicacion.idUbicacion.toString())
        )
    }

    fun eliminar(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseContract.UbicacionEntry.TABLE_NAME,
            "${DatabaseContract.UbicacionEntry.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun buscarPorVehiculo(idVehiculo: Int): Map<String, String>? {
        val db = dbHelper.readableDatabase
        val query = """
            SELECT 
                b.${DatabaseContract.BodegaEntry.COLUMN_NOMBRE}, 
                s.${DatabaseContract.SeccionEntry.COLUMN_NIVEL}, 
                u.${DatabaseContract.UbicacionEntry.COLUMN_FECHA},
                u.${DatabaseContract.UbicacionEntry.COLUMN_ID}
            FROM ${DatabaseContract.VehiculoEntry.TABLE_NAME} v
            JOIN ${DatabaseContract.UbicacionEntry.TABLE_NAME} u ON v.${DatabaseContract.VehiculoEntry.COLUMN_ID_UBICACION} = u.${DatabaseContract.UbicacionEntry.COLUMN_ID}
            JOIN ${DatabaseContract.SeccionEntry.TABLE_NAME} s ON u.${DatabaseContract.UbicacionEntry.COLUMN_ID_SECCION} = s.${DatabaseContract.SeccionEntry.COLUMN_ID}
            JOIN ${DatabaseContract.BodegaEntry.TABLE_NAME} b ON s.${DatabaseContract.SeccionEntry.COLUMN_ID_BODEGA} = b.${DatabaseContract.BodegaEntry.COLUMN_ID}
            WHERE v.${DatabaseContract.VehiculoEntry.COLUMN_ID} = ? AND u.${DatabaseContract.UbicacionEntry.COLUMN_ACTIVA} = 1
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(idVehiculo.toString()))
        var resultado: Map<String, String>? = null

        if (cursor.moveToFirst()) {
            resultado = mapOf(
                "bodega" to cursor.getString(0),
                "nivel" to cursor.getInt(1).toString(),
                "fecha" to cursor.getString(2),
                "idUbicacion" to cursor.getInt(3).toString()
            )
        }
        cursor.close()
        return resultado
    }
}
