package android.com.diego.notasdroid.datos

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.annotation.Nullable

class DatosDB (@Nullable context: Context?, @Nullable name: String?, @Nullable factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {

        createTable(db)

    }

    private fun createTable(db: SQLiteDatabase){

        val tablas = arrayOf(
            "CREATE TABLE $USER_TABLE (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT , name TEXT, img TEXT, pwd TEXT, ciclo INTEGER, curso INTEGER)",
            "CREATE TABLE $MODULE_TABLE (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT , nota REAL, img TEXT, profesor TEXT, aula INTEGER, ciclo INTEGER, curso INTEGER)",
            "CREATE TABLE $PRUEBAS_TABLE (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT , fecha TEXT, realizada INTEGER, nota REAL, idUser INTEGER, idModule INTEGER)"
        )

        for (item in tablas){
            db.execSQL(item)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    companion object{

        const val USER_TABLE = "users"
        const val MODULE_TABLE = "modules"
        const val PRUEBAS_TABLE = "pruebas"

    }

}