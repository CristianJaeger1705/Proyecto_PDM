package sv.edu.ues.fia.proyecto_pdm.taller

import android.content.ContentValues
import android.content.Context
import sv.edu.ues.fia.proyecto_pdm.DatabaseContract
import sv.edu.ues.fia.proyecto_pdm.DatabaseHelper

class TallerHandler(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun insertar(taller: Taller): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            // El ID es AUTOINCREMENT, no lo incluimos
            put(DatabaseContract.TallerEntry.COLUMN_NOMBRE, taller.nombreTaller)
            put(DatabaseContract.TallerEntry.COLUMN_DIRECCION, taller.direccion)
            put(DatabaseContract.TallerEntry.COLUMN_TELEFONO, taller.telefono)
            put(DatabaseContract.TallerEntry.COLUMN_AUTORIZADO, taller.autorizado)
        }
        return db.insert(DatabaseContract.TallerEntry.TABLE_NAME, null, values)
    }

    fun buscar(id: Int): Taller? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.TallerEntry.TABLE_NAME,
            null,
            "${DatabaseContract.TallerEntry.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        )
        var taller: Taller? = null
        if (cursor.moveToFirst()) {
            taller = Taller(
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TallerEntry.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TallerEntry.COLUMN_NOMBRE)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TallerEntry.COLUMN_DIRECCION)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TallerEntry.COLUMN_TELEFONO)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TallerEntry.COLUMN_AUTORIZADO))
            )
        }
        cursor.close()
        return taller
    }

    fun actualizar(taller: Taller): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.TallerEntry.COLUMN_NOMBRE, taller.nombreTaller)
            put(DatabaseContract.TallerEntry.COLUMN_DIRECCION, taller.direccion)
            put(DatabaseContract.TallerEntry.COLUMN_TELEFONO, taller.telefono)
            put(DatabaseContract.TallerEntry.COLUMN_AUTORIZADO, taller.autorizado)
        }
        return db.update(
            DatabaseContract.TallerEntry.TABLE_NAME,
            values,
            "${DatabaseContract.TallerEntry.COLUMN_ID} = ?",
            arrayOf(taller.idTaller.toString())
        )
    }

    fun eliminar(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseContract.TallerEntry.TABLE_NAME,
            "${DatabaseContract.TallerEntry.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun obtenerTodos(): List<Taller> {
        val lista = mutableListOf<Taller>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseContract.TallerEntry.TABLE_NAME}", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Taller(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TallerEntry.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TallerEntry.COLUMN_NOMBRE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TallerEntry.COLUMN_DIRECCION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TallerEntry.COLUMN_TELEFONO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TallerEntry.COLUMN_AUTORIZADO))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}
