package sv.edu.ues.fia.proyecto_pdm.transporte

import android.content.ContentValues
import android.content.Context
import sv.edu.ues.fia.proyecto_pdm.DatabaseHelper
import sv.edu.ues.fia.proyecto_pdm.DatabaseContract

class MedioTransporteHandler(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    // CREATE - Insertar un nuevo transporte
    fun insertar(medio: MedioTransporte): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            if (medio.idMedio != null) {
                put(DatabaseContract.MedioTransporteEntry.COLUMN_ID, medio.idMedio)
            }
            put(DatabaseContract.MedioTransporteEntry.COLUMN_TIPO, medio.tipo)
            put(DatabaseContract.MedioTransporteEntry.COLUMN_CAPACIDAD, medio.capacidadMax)
        }
        return db.insert(DatabaseContract.MedioTransporteEntry.TABLE_NAME, null, values)
    }

    // READ - Obtener todos los transportes
    fun obtenerTodos(): List<MedioTransporte> {
        val lista = mutableListOf<MedioTransporte>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseContract.MedioTransporteEntry.TABLE_NAME}", null)

        if (cursor.moveToFirst()) {
            do {
                val medio = MedioTransporte(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2)
                )
                lista.add(medio)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    // READ - Obtener uno por ID
    fun buscar(id: Int): MedioTransporte? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.MedioTransporteEntry.TABLE_NAME,
            null,
            "${DatabaseContract.MedioTransporteEntry.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        var medio: MedioTransporte? = null
        if (cursor.moveToFirst()) {
            medio = MedioTransporte(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getInt(2)
            )
        }
        cursor.close()
        return medio
    }

    // UPDATE - Actualizar un transporte
    fun actualizar(medio: MedioTransporte): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.MedioTransporteEntry.COLUMN_TIPO, medio.tipo)
            put(DatabaseContract.MedioTransporteEntry.COLUMN_CAPACIDAD, medio.capacidadMax)
        }
        val selection = "${DatabaseContract.MedioTransporteEntry.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(medio.idMedio.toString())
        return db.update(DatabaseContract.MedioTransporteEntry.TABLE_NAME, values, selection, selectionArgs)
    }

    // DELETE - Eliminar un transporte
    fun eliminar(id: Int): Int {
        val db = dbHelper.writableDatabase
        val selection = "${DatabaseContract.MedioTransporteEntry.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(DatabaseContract.MedioTransporteEntry.TABLE_NAME, selection, selectionArgs)
    }
}
