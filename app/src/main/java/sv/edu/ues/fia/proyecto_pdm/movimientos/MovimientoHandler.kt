package sv.edu.ues.fia.proyecto_pdm.movimientos

import android.content.ContentValues
import android.content.Context
import sv.edu.ues.fia.proyecto_pdm.DatabaseHelper
import sv.edu.ues.fia.proyecto_pdm.DatabaseContract

class MovimientoHandler(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun insertar(mov: Movimiento): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.MovimientoEntry.COLUMN_ID, mov.idMovimiento)
            put(DatabaseContract.MovimientoEntry.COLUMN_ID_MEDIO, mov.idMedio)
            put(DatabaseContract.MovimientoEntry.COLUMN_TIPO, mov.tipoMovimiento)
            put(DatabaseContract.MovimientoEntry.COLUMN_FECHA, mov.fecha)
            put(DatabaseContract.MovimientoEntry.COLUMN_HORA, mov.hora)
            put(DatabaseContract.MovimientoEntry.COLUMN_OBSERVACIONES, mov.observaciones)
        }
        return db.insert(DatabaseContract.MovimientoEntry.TABLE_NAME, null, values)
    }

    fun buscar(id: Int): Movimiento? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.MovimientoEntry.TABLE_NAME,
            null,
            "${DatabaseContract.MovimientoEntry.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        var mov: Movimiento? = null
        if (cursor.moveToFirst()) {
            mov = Movimiento(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5)
            )
        }
        cursor.close()
        return mov
    }

    fun actualizar(mov: Movimiento): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.MovimientoEntry.COLUMN_ID_MEDIO, mov.idMedio)
            put(DatabaseContract.MovimientoEntry.COLUMN_TIPO, mov.tipoMovimiento)
            put(DatabaseContract.MovimientoEntry.COLUMN_FECHA, mov.fecha)
            put(DatabaseContract.MovimientoEntry.COLUMN_HORA, mov.hora)
            put(DatabaseContract.MovimientoEntry.COLUMN_OBSERVACIONES, mov.observaciones)
        }
        return db.update(
            DatabaseContract.MovimientoEntry.TABLE_NAME, 
            values, 
            "${DatabaseContract.MovimientoEntry.COLUMN_ID} = ?", 
            arrayOf(mov.idMovimiento.toString())
        )
    }

    fun eliminar(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseContract.MovimientoEntry.TABLE_NAME, 
            "${DatabaseContract.MovimientoEntry.COLUMN_ID} = ?", 
            arrayOf(id.toString())
        )
    }

    fun agregarVehiculoAMovimiento(idMovimiento: Int, idVehiculo: Int): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.MovimientoVehiculoEntry.COLUMN_ID_MOVIMIENTO, idMovimiento)
            put(DatabaseContract.MovimientoVehiculoEntry.COLUMN_ID_VEHICULO, idVehiculo)
        }
        return db.insertOrThrow(DatabaseContract.MovimientoVehiculoEntry.TABLE_NAME, null, values)
    }

    fun obtenerVehiculosDeMovimiento(idMovimiento: Int): List<Int> {
        val lista = mutableListOf<Int>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.MovimientoVehiculoEntry.TABLE_NAME,
            arrayOf(DatabaseContract.MovimientoVehiculoEntry.COLUMN_ID_VEHICULO),
            "${DatabaseContract.MovimientoVehiculoEntry.COLUMN_ID_MOVIMIENTO} = ?",
            arrayOf(idMovimiento.toString()),
            null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                lista.add(cursor.getInt(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}
