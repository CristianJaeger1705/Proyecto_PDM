package sv.edu.ues.fia.proyecto_pdm

import android.content.ContentValues
import android.content.Context

class ImportadorHandler(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun insertar(importador: Importador): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.ImportadorEntry.COLUMN_NUI, importador.nui)
            put(DatabaseContract.ImportadorEntry.COLUMN_NOMBRE, importador.nombre)
        }
        return db.insert(DatabaseContract.ImportadorEntry.TABLE_NAME, null, values)
    }

    fun buscar(nui: String): Importador? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.ImportadorEntry.TABLE_NAME,
            null,
            "${DatabaseContract.ImportadorEntry.COLUMN_NUI} = ?",
            arrayOf(nui),
            null, null, null
        )
        var importador: Importador? = null
        if (cursor.moveToFirst()) {
            importador = Importador(cursor.getString(0), cursor.getString(1))
        }
        cursor.close()
        return importador
    }

    fun obtenerTodos(): List<Importador> {
        val lista = mutableListOf<Importador>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseContract.ImportadorEntry.TABLE_NAME}", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(Importador(cursor.getString(0), cursor.getString(1)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}
