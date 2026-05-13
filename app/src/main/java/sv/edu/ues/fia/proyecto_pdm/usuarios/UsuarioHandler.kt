package sv.edu.ues.fia.proyecto_pdm.usuarios

import android.content.ContentValues
import android.content.Context
import sv.edu.ues.fia.proyecto_pdm.DatabaseHelper
import sv.edu.ues.fia.proyecto_pdm.DatabaseContract

class UsuarioHandler(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun insertarUsuario(username: String, password: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.UsuarioEntry.COLUMN_USERNAME, username)
            put(DatabaseContract.UsuarioEntry.COLUMN_PASSWORD, password)
        }
        return db.insert(DatabaseContract.UsuarioEntry.TABLE_NAME, null, values)
    }

    fun validarLogin(username: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.UsuarioEntry.TABLE_NAME,
            null,
            "${DatabaseContract.UsuarioEntry.COLUMN_USERNAME} = ? AND ${DatabaseContract.UsuarioEntry.COLUMN_PASSWORD} = ?",
            arrayOf(username, password),
            null, null, null
        )
        val valid = cursor.count > 0
        cursor.close()
        return valid
    }
}
