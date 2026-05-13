package sv.edu.ues.fia.proyecto_pdm

import android.content.ContentValues
import android.content.Context

class VehiculoHandler(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun insertar(vehiculo: Vehiculo): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.VehiculoEntry.COLUMN_ID, vehiculo.idVehiculo)
            put(DatabaseContract.VehiculoEntry.COLUMN_MARCA, vehiculo.marca)
            put(DatabaseContract.VehiculoEntry.COLUMN_ESTADO, vehiculo.estado)
        }
        return db.insert(DatabaseContract.VehiculoEntry.TABLE_NAME, null, values)
    }

    fun buscar(id: Int): Vehiculo? {
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
            vehiculo = Vehiculo(cursor.getInt(0), cursor.getString(1), cursor.getString(2))
        }
        cursor.close()
        return vehiculo
    }
    
    fun obtenerTodos(): List<Vehiculo> {
        val lista = mutableListOf<Vehiculo>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseContract.VehiculoEntry.TABLE_NAME}", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(Vehiculo(cursor.getInt(0), cursor.getString(1), cursor.getString(2)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
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
}
