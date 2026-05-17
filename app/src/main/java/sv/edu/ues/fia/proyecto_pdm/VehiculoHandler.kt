package sv.edu.ues.fia.proyecto_pdm

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class VehiculoHandler(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun insertar(vehiculo: Vehiculo): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.VehiculoEntry.COLUMN_ID, vehiculo.idVehiculo)
            put(DatabaseContract.VehiculoEntry.COLUMN_MARCA, vehiculo.marca)
            put(DatabaseContract.VehiculoEntry.COLUMN_MODELO, vehiculo.modelo)
            put(DatabaseContract.VehiculoEntry.COLUMN_ANIO, vehiculo.anio)
            put(DatabaseContract.VehiculoEntry.COLUMN_ESTADO, vehiculo.estado)
            put(DatabaseContract.VehiculoEntry.COLUMN_ID_IMPORTACION, vehiculo.idImportacion)
        }
        return db.insert(DatabaseContract.VehiculoEntry.TABLE_NAME, null, values)
    }

    fun consultar(id: Int): Vehiculo? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.VehiculoEntry.TABLE_NAME,
            null,
            "${DatabaseContract.VehiculoEntry.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        
        var vehiculo: Vehiculo? = null
        if (cursor.moveToFirst()) {
            vehiculo = cursorToVehiculo(cursor)
        }
        cursor.close()
        return vehiculo
    }

    fun actualizar(vehiculo: Vehiculo): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.VehiculoEntry.COLUMN_MARCA, vehiculo.marca)
            put(DatabaseContract.VehiculoEntry.COLUMN_MODELO, vehiculo.modelo)
            put(DatabaseContract.VehiculoEntry.COLUMN_ANIO, vehiculo.anio)
            put(DatabaseContract.VehiculoEntry.COLUMN_ESTADO, vehiculo.estado)
            put(DatabaseContract.VehiculoEntry.COLUMN_ID_IMPORTACION, vehiculo.idImportacion)
        }
        return db.update(
            DatabaseContract.VehiculoEntry.TABLE_NAME,
            values,
            "${DatabaseContract.VehiculoEntry.COLUMN_ID} = ?",
            arrayOf(vehiculo.idVehiculo.toString())
        )
    }

    fun eliminar(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseContract.VehiculoEntry.TABLE_NAME,
            "${DatabaseContract.VehiculoEntry.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun obtenerTodos(): List<Vehiculo> {
        val lista = mutableListOf<Vehiculo>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseContract.VehiculoEntry.TABLE_NAME}", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(cursorToVehiculo(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    private fun cursorToVehiculo(cursor: Cursor): Vehiculo {
        return Vehiculo(
            idVehiculo = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.VehiculoEntry.COLUMN_ID)),
            marca = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.VehiculoEntry.COLUMN_MARCA)),
            modelo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.VehiculoEntry.COLUMN_MODELO)),
            anio = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.VehiculoEntry.COLUMN_ANIO)),
            estado = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.VehiculoEntry.COLUMN_ESTADO)),
            idUbicacion = if (cursor.isNull(cursor.getColumnIndexOrThrow(DatabaseContract.VehiculoEntry.COLUMN_ID_UBICACION))) null 
                          else cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.VehiculoEntry.COLUMN_ID_UBICACION)),
            idImportacion = if (cursor.isNull(cursor.getColumnIndexOrThrow(DatabaseContract.VehiculoEntry.COLUMN_ID_IMPORTACION))) null 
                            else cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.VehiculoEntry.COLUMN_ID_IMPORTACION))
        )
    }

    fun actualizarEstado(idVehiculo: Int, nuevoEstado: String): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.VehiculoEntry.COLUMN_ESTADO, nuevoEstado)
        }
        return db.update(
            DatabaseContract.VehiculoEntry.TABLE_NAME,
            values,
            "${DatabaseContract.VehiculoEntry.COLUMN_ID} = ?",
            arrayOf(idVehiculo.toString())
        )
    }

    fun asignarUbicacion(idVehiculo: Int, idUbicacion: Int?): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.VehiculoEntry.COLUMN_ID_UBICACION, idUbicacion)
        }
        return db.update(
            DatabaseContract.VehiculoEntry.TABLE_NAME,
            values,
            "${DatabaseContract.VehiculoEntry.COLUMN_ID} = ?",
            arrayOf(idVehiculo.toString())
        )
    }

    fun obtenerConteoPorEstado(): Map<String, Int> {
        val conteos = mutableMapOf("DISPONIBLE" to 0, "EN_REPARACION" to 0, "VENDIDO" to 0)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT ${DatabaseContract.VehiculoEntry.COLUMN_ESTADO}, COUNT(*) " +
                    "FROM ${DatabaseContract.VehiculoEntry.TABLE_NAME} " +
                    "GROUP BY ${DatabaseContract.VehiculoEntry.COLUMN_ESTADO}", null
        )
        if (cursor.moveToFirst()) {
            do {
                val estado = cursor.getString(0)
                val cantidad = cursor.getInt(1)
                conteos[estado] = cantidad
            } while (cursor.moveToNext())
        }
        cursor.close()
        return conteos
    }
}
