package sv.edu.ues.fia.proyecto_pdm.seccion

import android.content.ContentValues
import android.content.Context
import sv.edu.ues.fia.proyecto_pdm.DatabaseContract
import sv.edu.ues.fia.proyecto_pdm.DatabaseHelper

class SeccionHandler(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun insertar(seccion: Seccion): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.SeccionEntry.COLUMN_ID_BODEGA, seccion.idBodega)
            put(DatabaseContract.SeccionEntry.COLUMN_NIVEL, seccion.nivel)
            put(DatabaseContract.SeccionEntry.COLUMN_CAPACIDAD, seccion.capacidadMax)
        }
        return db.insert(DatabaseContract.SeccionEntry.TABLE_NAME, null, values)
    }

    fun consultar(id: Int): Seccion? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.SeccionEntry.TABLE_NAME,
            null,
            "${DatabaseContract.SeccionEntry.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        var seccion: Seccion? = null
        if (cursor.moveToFirst()) {
            seccion = Seccion(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getInt(2),
                cursor.getInt(3)
            )
        }
        cursor.close()
        return seccion
    }

    fun actualizar(seccion: Seccion): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.SeccionEntry.COLUMN_ID_BODEGA, seccion.idBodega)
            put(DatabaseContract.SeccionEntry.COLUMN_NIVEL, seccion.nivel)
            put(DatabaseContract.SeccionEntry.COLUMN_CAPACIDAD, seccion.capacidadMax)
        }
        return db.update(
            DatabaseContract.SeccionEntry.TABLE_NAME,
            values,
            "${DatabaseContract.SeccionEntry.COLUMN_ID} = ?",
            arrayOf(seccion.idSeccion.toString())
        )
    }

    fun eliminar(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseContract.SeccionEntry.TABLE_NAME,
            "${DatabaseContract.SeccionEntry.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun obtenerTodas(): List<Seccion> {
        val lista = mutableListOf<Seccion>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseContract.SeccionEntry.TABLE_NAME}", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(Seccion(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3)
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun obtenerPorBodega(idBodega: Int): List<Seccion> {
        val lista = mutableListOf<Seccion>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.SeccionEntry.TABLE_NAME,
            null,
            "${DatabaseContract.SeccionEntry.COLUMN_ID_BODEGA} = ?",
            arrayOf(idBodega.toString()),
            null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                lista.add(Seccion(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3)
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}
