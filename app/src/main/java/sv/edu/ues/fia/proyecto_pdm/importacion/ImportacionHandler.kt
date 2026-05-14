package sv.edu.ues.fia.proyecto_pdm.importacion

import android.content.ContentValues
import android.content.Context
import sv.edu.ues.fia.proyecto_pdm.DatabaseContract
import sv.edu.ues.fia.proyecto_pdm.DatabaseHelper

class ImportacionHandler(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun insertar(importacion: Importacion): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.ImportacionEntry.COLUMN_ID, importacion.idImportacion)
            put(DatabaseContract.ImportacionEntry.COLUMN_ID_IMPORTADOR, importacion.idImportador)
            put(DatabaseContract.ImportacionEntry.COLUMN_CANTIDAD_VEHICULOS, importacion.cantidadVehiculos)
            put(DatabaseContract.ImportacionEntry.COLUMN_FECHA, importacion.fecha)
        }
        return db.insert(DatabaseContract.ImportacionEntry.TABLE_NAME, null, values)
    }

    fun consultar(id: Int): Importacion? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.ImportacionEntry.TABLE_NAME,
            null,
            "${DatabaseContract.ImportacionEntry.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        var importacion: Importacion? = null
        if (cursor.moveToFirst()) {
            importacion = Importacion(
                idImportacion = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ImportacionEntry.COLUMN_ID)),
                idImportador = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ImportacionEntry.COLUMN_ID_IMPORTADOR)),
                cantidadVehiculos = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ImportacionEntry.COLUMN_CANTIDAD_VEHICULOS)),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ImportacionEntry.COLUMN_FECHA))
            )
        }
        cursor.close()
        return importacion
    }

    fun actualizar(importacion: Importacion): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.ImportacionEntry.COLUMN_ID_IMPORTADOR, importacion.idImportador)
            put(DatabaseContract.ImportacionEntry.COLUMN_CANTIDAD_VEHICULOS, importacion.cantidadVehiculos)
            put(DatabaseContract.ImportacionEntry.COLUMN_FECHA, importacion.fecha)
        }
        return db.update(
            DatabaseContract.ImportacionEntry.TABLE_NAME,
            values,
            "${DatabaseContract.ImportacionEntry.COLUMN_ID} = ?",
            arrayOf(importacion.idImportacion.toString())
        )
    }

    fun eliminar(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseContract.ImportacionEntry.TABLE_NAME,
            "${DatabaseContract.ImportacionEntry.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }
}
