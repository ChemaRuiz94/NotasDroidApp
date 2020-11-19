package android.com.diego.notasdroid.datos

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.preference.PreferenceManager
import androidx.annotation.Nullable

class DatosDB (@Nullable context: Context?, @Nullable name: String?, @Nullable factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {


    override fun onCreate(db: SQLiteDatabase) {

        createTable(db)

    }

    private fun createTable(db: SQLiteDatabase){

        val tablas = arrayOf(
            "CREATE TABLE $USER_TABLE (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT , name TEXT, img TEXT, pwd TEXT, ciclo INTEGER, curso INTEGER)",
            "CREATE TABLE IF NOT EXISTS $MODULE_TABLE (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT , nota REAL, img INTEGER, profesor TEXT, aula INTEGER, ciclo INTEGER, curso INTEGER)",
            "CREATE TABLE $PRUEBAS_TABLE (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT , fecha TEXT, realizada INTEGER, nota REAL, idUser TEXT, idModule TEXT)"
        )

        for (item in tablas){
            db.execSQL(item)
        }

    }

    private fun deleteTable(db: SQLiteDatabase){

        val tablas = arrayOf(
            "DROP TABLE IF EXISTS $USER_TABLE ",
            "DROP TABLE IF EXISTS $MODULE_TABLE",
            "DROP TABLE IF EXISTS $PRUEBAS_TABLE"
        )

        for (item in tablas){
            db.execSQL(item)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        deleteTable(db)
        createTable(db)

    }

    companion object{

        const val USER_TABLE = "users"
        const val MODULE_TABLE = "modules"
        const val PRUEBAS_TABLE = "pruebas"

    }

}