package sv.edu.ues.fia.proyecto_pdm

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createMedioTable = "CREATE TABLE ${DatabaseContract.MedioTransporteEntry.TABLE_NAME} (" +
                "${DatabaseContract.MedioTransporteEntry.COLUMN_ID} INTEGER PRIMARY KEY, " +
                "${DatabaseContract.MedioTransporteEntry.COLUMN_TIPO} TEXT, " +
                "${DatabaseContract.MedioTransporteEntry.COLUMN_CAPACIDAD} INTEGER)"
        db.execSQL(createMedioTable)

        val createUsuarioTable = "CREATE TABLE ${DatabaseContract.UsuarioEntry.TABLE_NAME} (" +
                "${DatabaseContract.UsuarioEntry.COLUMN_USERNAME} TEXT PRIMARY KEY, " +
                "${DatabaseContract.UsuarioEntry.COLUMN_PASSWORD} TEXT)"
        db.execSQL(createUsuarioTable)

        val createImportadorTable = "CREATE TABLE ${DatabaseContract.ImportadorEntry.TABLE_NAME} (" +
                "${DatabaseContract.ImportadorEntry.COLUMN_NUI} TEXT PRIMARY KEY, " +
                "${DatabaseContract.ImportadorEntry.COLUMN_NOMBRE} TEXT NOT NULL, " +
                "${DatabaseContract.ImportadorEntry.COLUMN_APELLIDO} TEXT NOT NULL, " +
                "${DatabaseContract.ImportadorEntry.COLUMN_APELLIDO_CASADA} TEXT, " +
                "${DatabaseContract.ImportadorEntry.COLUMN_GENERO} TEXT NOT NULL, " +
                "${DatabaseContract.ImportadorEntry.COLUMN_FECHA_NACIMIENTO} TEXT NOT NULL, " +
                "${DatabaseContract.ImportadorEntry.COLUMN_DIRECCION} TEXT NOT NULL, " +
                "${DatabaseContract.ImportadorEntry.COLUMN_CORREO} TEXT NOT NULL, " +
                "${DatabaseContract.ImportadorEntry.COLUMN_NUI_RESPONSABLE} TEXT, " +
                "FOREIGN KEY (${DatabaseContract.ImportadorEntry.COLUMN_NUI_RESPONSABLE}) " +
                "REFERENCES ${DatabaseContract.ImportadorEntry.TABLE_NAME}(${DatabaseContract.ImportadorEntry.COLUMN_NUI}))"
        db.execSQL(createImportadorTable)

        val createVehiculoTable = "CREATE TABLE ${DatabaseContract.VehiculoEntry.TABLE_NAME} (" +
                "${DatabaseContract.VehiculoEntry.COLUMN_ID} INTEGER PRIMARY KEY, " +
                "${DatabaseContract.VehiculoEntry.COLUMN_MARCA} TEXT, " +
                "${DatabaseContract.VehiculoEntry.COLUMN_ESTADO} TEXT DEFAULT 'DISPONIBLE', " +
                "${DatabaseContract.VehiculoEntry.COLUMN_ID_UBICACION} INTEGER, " +
                "FOREIGN KEY (${DatabaseContract.VehiculoEntry.COLUMN_ID_UBICACION}) REFERENCES ${DatabaseContract.UbicacionEntry.TABLE_NAME}(${DatabaseContract.UbicacionEntry.COLUMN_ID}) ON DELETE SET NULL)"
        db.execSQL(createVehiculoTable)

        val createVentaTable = "CREATE TABLE ${DatabaseContract.VentaEntry.TABLE_NAME} (" +
                "${DatabaseContract.VentaEntry.COLUMN_ID} INTEGER PRIMARY KEY, " +
                "${DatabaseContract.VentaEntry.COLUMN_ID_VEHICULO} INTEGER, " +
                "${DatabaseContract.VentaEntry.COLUMN_PRECIO} REAL, " +
                "${DatabaseContract.VentaEntry.COLUMN_ID_IMPORTADOR} TEXT(10), " +
                "${DatabaseContract.VentaEntry.COLUMN_FECHA} TEXT, " +
                "FOREIGN KEY (${DatabaseContract.VentaEntry.COLUMN_ID_VEHICULO}) REFERENCES ${DatabaseContract.VehiculoEntry.TABLE_NAME}(${DatabaseContract.VehiculoEntry.COLUMN_ID}), " +
                "FOREIGN KEY (${DatabaseContract.VentaEntry.COLUMN_ID_IMPORTADOR}) REFERENCES ${DatabaseContract.ImportadorEntry.TABLE_NAME}(${DatabaseContract.ImportadorEntry.COLUMN_NUI}))"
        db.execSQL(createVentaTable)

        val createMovimientoTable = "CREATE TABLE ${DatabaseContract.MovimientoEntry.TABLE_NAME} (" +
                "${DatabaseContract.MovimientoEntry.COLUMN_ID} INTEGER PRIMARY KEY, " +
                "${DatabaseContract.MovimientoEntry.COLUMN_ID_MEDIO} INTEGER, " +
                "${DatabaseContract.MovimientoEntry.COLUMN_TIPO} TEXT, " +
                "${DatabaseContract.MovimientoEntry.COLUMN_FECHA} TEXT, " +
                "${DatabaseContract.MovimientoEntry.COLUMN_HORA} TEXT, " +
                "${DatabaseContract.MovimientoEntry.COLUMN_OBSERVACIONES} TEXT, " +
                "FOREIGN KEY (${DatabaseContract.MovimientoEntry.COLUMN_ID_MEDIO}) REFERENCES ${DatabaseContract.MedioTransporteEntry.TABLE_NAME}(${DatabaseContract.MedioTransporteEntry.COLUMN_ID}))"
        db.execSQL(createMovimientoTable)

        val createMovVehTable = "CREATE TABLE ${DatabaseContract.MovimientoVehiculoEntry.TABLE_NAME} (" +
                "${DatabaseContract.MovimientoVehiculoEntry.COLUMN_ID_MOVIMIENTO} INTEGER, " +
                "${DatabaseContract.MovimientoVehiculoEntry.COLUMN_ID_VEHICULO} INTEGER, " +
                "PRIMARY KEY (${DatabaseContract.MovimientoVehiculoEntry.COLUMN_ID_MOVIMIENTO}, ${DatabaseContract.MovimientoVehiculoEntry.COLUMN_ID_VEHICULO}), " +
                "FOREIGN KEY (${DatabaseContract.MovimientoVehiculoEntry.COLUMN_ID_MOVIMIENTO}) REFERENCES ${DatabaseContract.MovimientoEntry.TABLE_NAME}(${DatabaseContract.MovimientoEntry.COLUMN_ID}), " +
                "FOREIGN KEY (${DatabaseContract.MovimientoVehiculoEntry.COLUMN_ID_VEHICULO}) REFERENCES ${DatabaseContract.VehiculoEntry.TABLE_NAME}(${DatabaseContract.VehiculoEntry.COLUMN_ID}))"
        db.execSQL(createMovVehTable)

        val createBodegaTable = "CREATE TABLE ${DatabaseContract.BodegaEntry.TABLE_NAME} (" +
                "${DatabaseContract.BodegaEntry.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${DatabaseContract.BodegaEntry.COLUMN_NOMBRE} TEXT, " +
                "${DatabaseContract.BodegaEntry.COLUMN_DEPARTAMENTO} TEXT, " +
                "${DatabaseContract.BodegaEntry.COLUMN_DIRECCION} TEXT, " +
                "${DatabaseContract.BodegaEntry.COLUMN_CAPACIDAD} INTEGER)"
        db.execSQL(createBodegaTable)

        val createSeccionTable = "CREATE TABLE ${DatabaseContract.SeccionEntry.TABLE_NAME} (" +
                "${DatabaseContract.SeccionEntry.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${DatabaseContract.SeccionEntry.COLUMN_ID_BODEGA} INTEGER, " +
                "${DatabaseContract.SeccionEntry.COLUMN_NIVEL} INTEGER, " +
                "${DatabaseContract.SeccionEntry.COLUMN_CAPACIDAD} INTEGER, " +
                "FOREIGN KEY (${DatabaseContract.SeccionEntry.COLUMN_ID_BODEGA}) REFERENCES ${DatabaseContract.BodegaEntry.TABLE_NAME}(${DatabaseContract.BodegaEntry.COLUMN_ID}) ON DELETE CASCADE)"
        db.execSQL(createSeccionTable)

        val createUbicacionTable = "CREATE TABLE ${DatabaseContract.UbicacionEntry.TABLE_NAME} (" +
                "${DatabaseContract.UbicacionEntry.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${DatabaseContract.UbicacionEntry.COLUMN_ID_SECCION} INTEGER, " +
                "${DatabaseContract.UbicacionEntry.COLUMN_FECHA} TEXT, " +
                "${DatabaseContract.UbicacionEntry.COLUMN_ACTIVA} INTEGER, " +
                "FOREIGN KEY (${DatabaseContract.UbicacionEntry.COLUMN_ID_SECCION}) REFERENCES ${DatabaseContract.SeccionEntry.TABLE_NAME}(${DatabaseContract.SeccionEntry.COLUMN_ID}) ON DELETE CASCADE)"
        db.execSQL(createUbicacionTable)

        // TRIGGER 1: Descargo automático al vender
        val trVentaInsert = """
            CREATE TRIGGER tr_venta_descargo AFTER INSERT ON ${DatabaseContract.VentaEntry.TABLE_NAME}
            BEGIN
                UPDATE ${DatabaseContract.VehiculoEntry.TABLE_NAME} 
                SET ${DatabaseContract.VehiculoEntry.COLUMN_ESTADO} = 'VENDIDO' 
                WHERE ${DatabaseContract.VehiculoEntry.COLUMN_ID} = NEW.${DatabaseContract.VentaEntry.COLUMN_ID_VEHICULO};
            END;
        """.trimIndent()
        db.execSQL(trVentaInsert)

        // TRIGGER 2: Restaurar stock al eliminar venta
        val trVentaDelete = """
            CREATE TRIGGER tr_venta_restaurar AFTER DELETE ON ${DatabaseContract.VentaEntry.TABLE_NAME}
            BEGIN
                UPDATE ${DatabaseContract.VehiculoEntry.TABLE_NAME} 
                SET ${DatabaseContract.VehiculoEntry.COLUMN_ESTADO} = 'DISPONIBLE' 
                WHERE ${DatabaseContract.VehiculoEntry.COLUMN_ID} = OLD.${DatabaseContract.VentaEntry.COLUMN_ID_VEHICULO};
            END;
        """.trimIndent()
        db.execSQL(trVentaDelete)

        // TRIGGER 3: Validar capacidad máxima del transporte
        val trCapacidad = """
            CREATE TRIGGER tr_validar_capacidad BEFORE INSERT ON ${DatabaseContract.MovimientoVehiculoEntry.TABLE_NAME}
            BEGIN
                SELECT CASE
                    WHEN (SELECT COUNT(*) FROM ${DatabaseContract.MovimientoVehiculoEntry.TABLE_NAME} WHERE ${DatabaseContract.MovimientoVehiculoEntry.COLUMN_ID_MOVIMIENTO} = NEW.${DatabaseContract.MovimientoVehiculoEntry.COLUMN_ID_MOVIMIENTO}) >= 
                         (SELECT mt.${DatabaseContract.MedioTransporteEntry.COLUMN_CAPACIDAD} FROM ${DatabaseContract.MedioTransporteEntry.TABLE_NAME} mt 
                          JOIN ${DatabaseContract.MovimientoEntry.TABLE_NAME} m ON mt.${DatabaseContract.MedioTransporteEntry.COLUMN_ID} = m.${DatabaseContract.MovimientoEntry.COLUMN_ID_MEDIO} 
                          WHERE m.${DatabaseContract.MovimientoEntry.COLUMN_ID} = NEW.${DatabaseContract.MovimientoVehiculoEntry.COLUMN_ID_MOVIMIENTO})
                    THEN RAISE(ABORT, 'Capacidad máxima del transporte alcanzada')
                END;
            END;
        """.trimIndent()
        db.execSQL(trCapacidad)

        // TRIGGER 4: Validar capacidad máxima de la sección
        val trCapacidadSeccion = """
            CREATE TRIGGER tr_validar_capacidad_seccion BEFORE INSERT ON ${DatabaseContract.UbicacionEntry.TABLE_NAME}
            BEGIN
                SELECT CASE
                    WHEN (SELECT COUNT(*) FROM ${DatabaseContract.UbicacionEntry.TABLE_NAME} 
                          WHERE ${DatabaseContract.UbicacionEntry.COLUMN_ID_SECCION} = NEW.${DatabaseContract.UbicacionEntry.COLUMN_ID_SECCION} 
                          AND ${DatabaseContract.UbicacionEntry.COLUMN_ACTIVA} = 1) >= 
                         (SELECT ${DatabaseContract.SeccionEntry.COLUMN_CAPACIDAD} FROM ${DatabaseContract.SeccionEntry.TABLE_NAME} 
                          WHERE ${DatabaseContract.SeccionEntry.COLUMN_ID} = NEW.${DatabaseContract.UbicacionEntry.COLUMN_ID_SECCION})
                    THEN RAISE(ABORT, 'Capacidad máxima de la sección alcanzada')
                END;
            END;
        """.trimIndent()
        db.execSQL(trCapacidadSeccion)

        // TRIGGER 5: Validar cantidad máxima de secciones por bodega
        val trMaxSecciones = """
            CREATE TRIGGER tr_validar_max_secciones BEFORE INSERT ON ${DatabaseContract.SeccionEntry.TABLE_NAME}
            BEGIN
                SELECT CASE
                    WHEN (SELECT COUNT(*) FROM ${DatabaseContract.SeccionEntry.TABLE_NAME} 
                          WHERE ${DatabaseContract.SeccionEntry.COLUMN_ID_BODEGA} = NEW.${DatabaseContract.SeccionEntry.COLUMN_ID_BODEGA}) >= 
                         (SELECT ${DatabaseContract.BodegaEntry.COLUMN_CAPACIDAD} FROM ${DatabaseContract.BodegaEntry.TABLE_NAME} 
                          WHERE ${DatabaseContract.BodegaEntry.COLUMN_ID} = NEW.${DatabaseContract.SeccionEntry.COLUMN_ID_BODEGA})
                    THEN RAISE(ABORT, 'Capacidad máxima de secciones en la bodega alcanzada')
                END;
            END;
        """.trimIndent()
        db.execSQL(trMaxSecciones)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        if (!db.isReadOnly) {
            db.execSQL("PRAGMA foreign_keys=ON;")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.MedioTransporteEntry.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.UsuarioEntry.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.ImportadorEntry.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.VehiculoEntry.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.MovimientoEntry.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.MovimientoVehiculoEntry.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.VentaEntry.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.UbicacionEntry.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.SeccionEntry.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.BodegaEntry.TABLE_NAME}")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "proyecto_pdm.db"
        private const val DATABASE_VERSION = 14

        @Volatile
        private var INSTANCE: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = DatabaseHelper(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}
