package sv.edu.ues.fia.proyecto_pdm.ventas

import android.content.ContentValues
import android.content.Context
import sv.edu.ues.fia.proyecto_pdm.DatabaseContract
import sv.edu.ues.fia.proyecto_pdm.DatabaseHelper

class VentaHandler(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun registrarVenta(venta: Venta): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.VentaEntry.COLUMN_ID, venta.idVenta)
            put(DatabaseContract.VentaEntry.COLUMN_ID_VEHICULO, venta.idVehiculo)
            put(DatabaseContract.VentaEntry.COLUMN_PRECIO, venta.precioVenta)
            put(DatabaseContract.VentaEntry.COLUMN_ID_IMPORTADOR, venta.nuiImportador)
            put(DatabaseContract.VentaEntry.COLUMN_FECHA, venta.fechaVenta)
        }
        return db.insert(DatabaseContract.VentaEntry.TABLE_NAME, null, values)
    }

    fun buscarVenta(id: Int): Venta? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.VentaEntry.TABLE_NAME,
            null,
            "${DatabaseContract.VentaEntry.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        var venta: Venta? = null
        if (cursor.moveToFirst()) {
            venta = Venta(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getDouble(2),
                cursor.getString(3),
                cursor.getString(4)
            )
        }
        cursor.close()
        return venta
    }

    fun actualizarVenta(venta: Venta): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.VentaEntry.COLUMN_ID_VEHICULO, venta.idVehiculo)
            put(DatabaseContract.VentaEntry.COLUMN_PRECIO, venta.precioVenta)
            put(DatabaseContract.VentaEntry.COLUMN_ID_IMPORTADOR, venta.nuiImportador)
            put(DatabaseContract.VentaEntry.COLUMN_FECHA, venta.fechaVenta)
        }
        return db.update(
            DatabaseContract.VentaEntry.TABLE_NAME,
            values,
            "${DatabaseContract.VentaEntry.COLUMN_ID} = ?",
            arrayOf(venta.idVenta.toString())
        )
    }

    fun eliminarVenta(idVenta: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseContract.VentaEntry.TABLE_NAME,
            "${DatabaseContract.VentaEntry.COLUMN_ID} = ?",
            arrayOf(idVenta.toString())
        )
    }

    fun obtenerTodas(): List<Venta> {
        val lista = mutableListOf<Venta>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseContract.VentaEntry.TABLE_NAME}", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(Venta(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4)
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}
