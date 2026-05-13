package sv.edu.ues.fia.proyecto_pdm.reparacion

import android.content.ContentValues
import android.content.Context
import sv.edu.ues.fia.proyecto_pdm.DatabaseContract
import sv.edu.ues.fia.proyecto_pdm.DatabaseHelper

class ReparacionHandler(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun insertar(reparacion: Reparacion): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            // El ID es AUTOINCREMENT
            put(DatabaseContract.ReparacionEntry.COLUMN_ID_TALLER, reparacion.idTaller)
            put(DatabaseContract.ReparacionEntry.COLUMN_ID_VEHICULO, reparacion.idVehiculo)
            put(DatabaseContract.ReparacionEntry.COLUMN_FECHA_ENTRADA, reparacion.fechaEntrada)
            put(DatabaseContract.ReparacionEntry.COLUMN_FECHA_SALIDA, reparacion.fechaSalida)
            put(DatabaseContract.ReparacionEntry.COLUMN_DESCRIPCION, reparacion.descripcionTrabajo)
            put(DatabaseContract.ReparacionEntry.COLUMN_APTO, reparacion.aptoParaVenta)
            put(DatabaseContract.ReparacionEntry.COLUMN_COSTO, reparacion.costo)
        }
        return db.insert(DatabaseContract.ReparacionEntry.TABLE_NAME, null, values)
    }

    fun buscar(id: Int): Reparacion? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.ReparacionEntry.TABLE_NAME,
            null,
            "${DatabaseContract.ReparacionEntry.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        var reparacion: Reparacion? = null
        if (cursor.moveToFirst()) {
            reparacion = Reparacion(
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_ID_TALLER)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_ID_VEHICULO)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_FECHA_ENTRADA)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_FECHA_SALIDA)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_DESCRIPCION)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_APTO)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_COSTO))
            )
        }
        cursor.close()
        return reparacion
    }

    fun actualizar(reparacion: Reparacion): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.ReparacionEntry.COLUMN_ID_TALLER, reparacion.idTaller)
            put(DatabaseContract.ReparacionEntry.COLUMN_ID_VEHICULO, reparacion.idVehiculo)
            put(DatabaseContract.ReparacionEntry.COLUMN_FECHA_ENTRADA, reparacion.fechaEntrada)
            put(DatabaseContract.ReparacionEntry.COLUMN_FECHA_SALIDA, reparacion.fechaSalida)
            put(DatabaseContract.ReparacionEntry.COLUMN_DESCRIPCION, reparacion.descripcionTrabajo)
            put(DatabaseContract.ReparacionEntry.COLUMN_APTO, reparacion.aptoParaVenta)
            put(DatabaseContract.ReparacionEntry.COLUMN_COSTO, reparacion.costo)
        }
        return db.update(
            DatabaseContract.ReparacionEntry.TABLE_NAME,
            values,
            "${DatabaseContract.ReparacionEntry.COLUMN_ID} = ?",
            arrayOf(reparacion.idReparacion.toString())
        )
    }

    fun eliminar(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseContract.ReparacionEntry.TABLE_NAME,
            "${DatabaseContract.ReparacionEntry.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun obtenerTodos(): List<Reparacion> {
        val lista = mutableListOf<Reparacion>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseContract.ReparacionEntry.TABLE_NAME}", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Reparacion(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_ID_TALLER)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_ID_VEHICULO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_FECHA_ENTRADA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_FECHA_SALIDA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_DESCRIPCION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_APTO)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.ReparacionEntry.COLUMN_COSTO))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}
