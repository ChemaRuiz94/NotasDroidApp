package android.com.diego.notasdroid.datos

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.text.SimpleDateFormat

object SQLiteControlador {

    // Variables de
    private const val DATOS_BD = "DATOS_BD_LITE"
    private const val DATOS_BD_VERSION = 10

    @SuppressLint("Recycle")
    fun selectUsuario(email: String?, context: Context?): UserSQLite? {

        var user : UserSQLite? = null
        val bdDatos = DatosDB(context, DATOS_BD, null, DATOS_BD_VERSION)
        val bd = bdDatos.readableDatabase

        val args = arrayOf(email)

        //val c = bd.query(DatosDB.USER_TABLE, null, null, null, null, filtro, null)
        val c = bd.rawQuery("SELECT * FROM " + DatosDB.USER_TABLE + " WHERE email = ?" , args)

        if (c.moveToFirst()){
            do {
                user = UserSQLite(c.getString(1), c.getString(2), c.getString(3), c.getString(4),
                    c.getInt(5), c.getInt(6))

            }while(c.moveToNext())
        }

        bd.close()
        bdDatos.close()

        return user

    }

    fun insertUsuario(user : UserSQLite, context: Context?) : Boolean{
        // Abrimos la BD en modo escritura
        val bdDatos = DatosDB(context, DATOS_BD, null, DATOS_BD_VERSION)
        val bd: SQLiteDatabase = bdDatos.writableDatabase
        var sal = false

        try {

            val values = ContentValues()
            values.put("email", user.email)
            values.put("name", user.name)
            values.put("img", user.img)
            values.put("pwd", user.pwd)
            values.put("ciclo", user.ciclo)
            values.put("curso", user.curso)

            val res = bd.insert(DatosDB.USER_TABLE, null, values)
            sal = true

        } catch (ex: SQLException) {
            Log.d("Datos", "Error al insertar un nuevo Dato " + ex.message)
        } finally {
            bd.close()
            bdDatos.close()
            return sal
        }
    }

    fun insertModulo(module: ModuloSQLite , context: Context?) : Boolean{
        // Abrimos la BD en modo escritura
        val bdDatos = DatosDB(context, DATOS_BD, null, DATOS_BD_VERSION)
        val bd: SQLiteDatabase = bdDatos.writableDatabase
        var sal = false

        try {

            val values = ContentValues()
            values.put("nombre", module.nombre)
            values.put("nota", module.nota)
            //values.put("img", module.img)
            values.put("profesor", module.profesor)
            values.put("aula", module.aula)
            values.put("ciclo", module.ciclo)
            values.put("curso", module.curso)

            val res = bd.insert(DatosDB.MODULE_TABLE, null, values)
            sal = true

        } catch (ex: SQLException) {
            Log.d("Datos", "Error al insertar un nuevo Dato " + ex.message)
        } finally {
            bd.close()
            bdDatos.close()
            return sal
        }
    }

    @SuppressLint("Recycle")
    fun selectModulos(ciclo: String?, curso: String?, context: Context?): MutableList<ModuloSQLite>? {

        val modulos = mutableListOf<ModuloSQLite>()
        val bdDatos = DatosDB(context, DATOS_BD, null, DATOS_BD_VERSION)
        val bd = bdDatos.readableDatabase

        val args = arrayOf(ciclo, curso)

        //val c = bd.query(DatosDB.USER_TABLE, null, null, null, null, filtro, null)
        val c = bd.rawQuery("SELECT * FROM " + DatosDB.MODULE_TABLE + " WHERE ciclo = ? AND curso = ?" , args)

        if (c.moveToFirst()){
            do {
                val modulo = ModuloSQLite(c.getString(1), c.getDouble(2), c.getInt(3), c.getString(4),
                    c.getInt(5), c.getInt(6), c.getInt(7))
                modulos.add(modulo)

            }while(c.moveToNext())
        }

        bd.close()
        bdDatos.close()

        return modulos

    }

    @SuppressLint("Recycle")
    fun selectPruebas(idUser: String?, idModulo: String?, context: Context?): MutableList<PruebaSQLite>? {

        val pruebas = mutableListOf<PruebaSQLite>()
        val bdDatos = DatosDB(context, DATOS_BD, null, DATOS_BD_VERSION)
        val bd = bdDatos.readableDatabase

        val args = arrayOf(idUser, idModulo)

        //val c = bd.query(DatosDB.USER_TABLE, null, null, null, null, filtro, null)
        val c = bd.rawQuery("SELECT * FROM " + DatosDB.PRUEBAS_TABLE + " WHERE idUser = ? AND idModule = ?" , args)

        if (c.moveToFirst()){
            do {
                val prueba = PruebaSQLite(c.getString(1), c.getString(2), c.getInt(3), c.getDouble(4),
                    c.getString(5), c.getString(6))
                pruebas.add(prueba)

            }while(c.moveToNext())
        }

        bd.close()
        bdDatos.close()

        return pruebas

    }


    @SuppressLint("SimpleDateFormat")
    fun insertPrueba(prueba: PruebaSQLite, context: Context?) : Boolean{
        // Abrimos la BD en modo escritura
        val bdDatos = DatosDB(context, DATOS_BD, null, DATOS_BD_VERSION)
        val bd: SQLiteDatabase = bdDatos.writableDatabase
        var sal = false
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")

        try {

            val values = ContentValues()
            values.put("nombre", prueba.nombre)
            values.put("fecha", dateFormat.format(prueba.fecha))
            values.put("realizada", prueba.realizada)
            values.put("nota", prueba.nota)
            values.put("idUser", prueba.idUser)
            values.put("idModule", prueba.idModule)

            val res = bd.insert(DatosDB.PRUEBAS_TABLE, null, values)
            sal = true

        } catch (ex: SQLException) {
            Log.d("Datos", "Error al insertar un nuevo Dato " + ex.message)
        } finally {
            bd.close()
            bdDatos.close()
            return sal
        }
    }

    /**
     * Actualiza un lugar en el sistema de almacenamiento
     */
    @SuppressLint("SimpleDateFormat")
    fun updatePrueba(pruebaNew: PruebaSQLite, pruebaOld: PruebaSQLite, context: Context?): Boolean {
        // Abrimos la BD en modo escritura
        val bdDatos = DatosDB(context, DATOS_BD, null, DATOS_BD_VERSION)
        val bd: SQLiteDatabase = bdDatos.writableDatabase
        var sal = false
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")

        try {
            // Cargamos los valores
            val values = ContentValues()
            values.put("nombre", pruebaNew.nombre)
            values.put("fecha", dateFormat.format(pruebaNew.fecha))
            values.put("realizada", pruebaNew.realizada)
            values.put("nota", pruebaNew.nota)
            values.put("idUser", pruebaNew.idUser)
            values.put("idModule", pruebaNew.idModule)
            // Creamos el where
            val where = "idUser = ? and idModule = ? and nombre = ?"
            //Cargamos los parámetros es un vector, en este caso es solo uno, pero podrían ser mas
            val args  = arrayOf(pruebaOld.idUser.toString(), pruebaOld.idModule.toString(), pruebaOld.nombre)
            // En el fondo hemos hecho where descripción = dato.descripcion, podíamos haber usado el id
            // Eliminamos. En res tenemos el numero de filas eliminadas por si queremos tenerlo en cuenta
            val res = bd.update(DatosDB.PRUEBAS_TABLE, values, where, args)
            sal = true
        } catch (ex: SQLException) {
            Log.d("Datos", "Error al actualizar este lugar " + ex.message)
        } finally {
            bd.close()
            bdDatos.close()
            return sal
        }
    }

    /**
     * Elimina un lugar del sistema de almacenamiento
     */
    fun deletePrueba(prueba: PruebaSQLite, context: Context?): Boolean {
        // Abrimos la BD en modo escritura
        val bdDatos = DatosDB(context, DATOS_BD, null, DATOS_BD_VERSION)
        val bd: SQLiteDatabase = bdDatos.writableDatabase
        var sal = false
        try {

            // Creamos el where
            val where = "idUser = ? and idModule = ? and nombre = ?"
            //Cargamos los parámetros es un vector, en este caso es solo uno, pero podrían ser mas
            val args  = arrayOf(prueba.idUser.toString(), prueba.idModule.toString(), prueba.nombre)
            // En el fondo hemos hecho where descripción = dato.descripcion, podíamos habrr usado el id
            // Eliminamos. En res tenemos el numero de filas eliminadas por si queremos tenerlo en cuenta
            val res = bd.delete(DatosDB.PRUEBAS_TABLE, where, args)
            sal = true
        } catch (ex: SQLException) {
            Log.d("Datos", "Error al eliminar este Dato " + ex.message)
        } finally {
            bd.close()
            bdDatos.close()
            return sal
        }
    }

    fun removeAll(context: Context?): Boolean {
        // Abrimos la BD en modo escritura
        val bdDatos = DatosDB(context, DATOS_BD, null, DATOS_BD_VERSION)
        val bd: SQLiteDatabase = bdDatos.writableDatabase
        var sal = false
        try {
            bd.execSQL("DELETE FROM ${DatosDB.MODULE_TABLE}")
            sal = true
        } catch (ex: SQLException) {
            Log.d("Datos", "Error al borrar todos los datos " + ex.message)
        } finally {
            bd.close()
            bdDatos.close()
            return sal
        }
    }

}