package sv.edu.ues.fia.proyecto_pdm.bodega

import android.content.ContentValues
import android.content.Context
import sv.edu.ues.fia.proyecto_pdm.DatabaseContract
import sv.edu.ues.fia.proyecto_pdm.DatabaseHelper

class BodegaHandler(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun insertar(bodega: Bodega): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.BodegaEntry.COLUMN_NOMBRE, bodega.nombreBodega)
            put(DatabaseContract.BodegaEntry.COLUMN_DEPARTAMENTO, bodega.departamento)
            put(DatabaseContract.BodegaEntry.COLUMN_DIRECCION, bodega.direccion)
            put(DatabaseContract.BodegaEntry.COLUMN_CAPACIDAD, bodega.capacidadSecciones)
        }
        return db.insert(DatabaseContract.BodegaEntry.TABLE_NAME, null, values)
    }

    fun consultar(id: Int): Bodega? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.BodegaEntry.TABLE_NAME,
            null,
            "${DatabaseContract.BodegaEntry.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        
        var bodega: Bodega? = null
        if (cursor.moveToFirst()) {
            bodega = Bodega().apply {
                idBodega = cursor.getInt(0)
                nombreBodega = cursor.getString(1)
                departamento = cursor.getString(2)
                direccion = cursor.getString(3)
                capacidadSecciones = cursor.getInt(4)
            }
        }
        cursor.close()
        return bodega
    }

    fun actualizar(bodega: Bodega): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.BodegaEntry.COLUMN_NOMBRE, bodega.nombreBodega)
            put(DatabaseContract.BodegaEntry.COLUMN_DEPARTAMENTO, bodega.departamento)
            put(DatabaseContract.BodegaEntry.COLUMN_DIRECCION, bodega.direccion)
        }
        return db.update(
            DatabaseContract.BodegaEntry.TABLE_NAME,
            values,
            "${DatabaseContract.BodegaEntry.COLUMN_ID} = ?",
            arrayOf(bodega.idBodega.toString())
        )
    }

    fun eliminar(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseContract.BodegaEntry.TABLE_NAME,
            "${DatabaseContract.BodegaEntry.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun obtenerConteoVehiculos(idBodega: Int): Int {
        val db = dbHelper.readableDatabase
        val query = """
            SELECT COUNT(*) 
            FROM ${DatabaseContract.VehiculoEntry.TABLE_NAME} v
            JOIN ${DatabaseContract.UbicacionEntry.TABLE_NAME} u ON v.${DatabaseContract.VehiculoEntry.COLUMN_ID_UBICACION} = u.${DatabaseContract.UbicacionEntry.COLUMN_ID}
            JOIN ${DatabaseContract.SeccionEntry.TABLE_NAME} s ON u.${DatabaseContract.UbicacionEntry.COLUMN_ID_SECCION} = s.${DatabaseContract.SeccionEntry.COLUMN_ID}
            WHERE s.${DatabaseContract.SeccionEntry.COLUMN_ID_BODEGA} = ? AND u.${DatabaseContract.UbicacionEntry.COLUMN_ACTIVA} = 1
        """.trimIndent()
        
        val cursor = db.rawQuery(query, arrayOf(idBodega.toString()))
        var conteo = 0
        if (cursor.moveToFirst()) {
            conteo = cursor.getInt(0)
        }
        cursor.close()
        return conteo
    }

    fun obtenerTodas(): List<Bodega> {
        val lista = mutableListOf<Bodega>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseContract.BodegaEntry.TABLE_NAME}", null)
        if (cursor.moveToFirst()) {
            do {
                val bodega = Bodega().apply {
                    idBodega = cursor.getInt(0)
                    nombreBodega = cursor.getString(1)
                    departamento = cursor.getString(2)
                    direccion = cursor.getString(3)
                    capacidadSecciones = cursor.getInt(4)
                }
                lista.add(bodega)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}
