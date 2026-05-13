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
            put(DatabaseContract.ImportadorEntry.COLUMN_APELLIDO, importador.apellido)
            put(DatabaseContract.ImportadorEntry.COLUMN_APELLIDO_CASADA, importador.apellidoCasada)
            put(DatabaseContract.ImportadorEntry.COLUMN_GENERO, importador.genero)
            put(DatabaseContract.ImportadorEntry.COLUMN_FECHA_NACIMIENTO, importador.fechaNacimiento)
            put(DatabaseContract.ImportadorEntry.COLUMN_DIRECCION, importador.direccion)
            put(DatabaseContract.ImportadorEntry.COLUMN_CORREO, importador.correoElectronico)
            put(DatabaseContract.ImportadorEntry.COLUMN_NUI_RESPONSABLE, importador.nuiResponsable)
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
            importador = Importador(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8)
            )
        }
        cursor.close()
        return importador
    }

    fun actualizar(importador: Importador): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.ImportadorEntry.COLUMN_NOMBRE, importador.nombre)
            put(DatabaseContract.ImportadorEntry.COLUMN_APELLIDO, importador.apellido)
            put(DatabaseContract.ImportadorEntry.COLUMN_APELLIDO_CASADA, importador.apellidoCasada)
            put(DatabaseContract.ImportadorEntry.COLUMN_GENERO, importador.genero)
            put(DatabaseContract.ImportadorEntry.COLUMN_FECHA_NACIMIENTO, importador.fechaNacimiento)
            put(DatabaseContract.ImportadorEntry.COLUMN_DIRECCION, importador.direccion)
            put(DatabaseContract.ImportadorEntry.COLUMN_CORREO, importador.correoElectronico)
            put(DatabaseContract.ImportadorEntry.COLUMN_NUI_RESPONSABLE, importador.nuiResponsable)
        }
        return db.update(
            DatabaseContract.ImportadorEntry.TABLE_NAME,
            values,
            "${DatabaseContract.ImportadorEntry.COLUMN_NUI} = ?",
            arrayOf(importador.nui)
        )
    }

    fun eliminar(nui: String): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseContract.ImportadorEntry.TABLE_NAME,
            "${DatabaseContract.ImportadorEntry.COLUMN_NUI} = ?",
            arrayOf(nui)
        )
    }

    fun obtenerTodos(): List<Importador> {
        val lista = mutableListOf<Importador>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseContract.ImportadorEntry.TABLE_NAME}", null
        )
        if (cursor.moveToFirst()) {
            do {
                lista.add(Importador(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8)
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}