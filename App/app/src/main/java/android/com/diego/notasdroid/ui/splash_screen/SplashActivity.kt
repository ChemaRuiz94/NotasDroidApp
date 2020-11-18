package android.com.diego.notasdroid.ui.splash_screen

import android.com.diego.notasdroid.R
import android.com.diego.notasdroid.datos.ModuloSQLite
import android.com.diego.notasdroid.datos.SQLiteControlador
import android.com.diego.notasdroid.login.LoginActivity
import android.com.diego.notasdroid.signUp.SignUp
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log

class SplashActivity : AppCompatActivity() {

    /*private var primeroDam = resources.getStringArray(R.array.primeroDam)
    private var profesorPrimeroDam = resources.getStringArray(R.array.profesorPrimeroDam)
    private var segundoDam = resources.getStringArray(R.array.segundoDam)
    private var profesorSegundoDam = resources.getStringArray(R.array.profesorSegundoDam)*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        SystemClock.sleep(4000)

        SQLiteControlador.removeAll(this)
        initModulos()
        /*val newModule = ModuloSQLite("Acceso a Datos", 0.0, R.drawable.acceso, "Joaquin Rubio", 209,1, 2)
        SQLiteControlador.insertModulo(newModule, this)
        val newModule1 = ModuloSQLite("Sistemas de Gestión Empresarial", 0.0, R.drawable.sge, "Inma Gijon", 209,1, 2)
        SQLiteControlador.insertModulo(newModule1, this)*/

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initModulos(){
        //crearModulosPrimeroDam()
        crearModulosSegundoDam()
    }

    private fun crearModulosPrimeroDam(){

        val primeroDam = resources.getStringArray(R.array.primeroDam)
        val profesorPrimeroDam = resources.getStringArray(R.array.profesorPrimeroDam)

        //(var nombre : String, var nota : Double, var img : Int, var profesor : String, var aula : Int, var ciclo : Int, var curso : Int)
        for (item in profesorPrimeroDam){
            var i = 0
            val profesor = profesorPrimeroDam[i]
            val newModule = ModuloSQLite(item.toString(), 0.0, R.drawable.ic_menu_camera, profesor, 213,1, 1)
            SQLiteControlador.insertModulo(newModule, this)
            i+=1

        }

    }
    private fun crearModulosSegundoDam(){
        val segundoDam = resources.getStringArray(R.array.segundoDam)
        val profesorSegundoDam = resources.getStringArray(R.array.profesorSegundoDam)
        //(var nombre : String, var nota : Double, var img : Int, var profesor : String, var aula : Int, var ciclo : Int, var curso : Int)
        for (item in segundoDam){
            var i = 0
            val profesor = profesorSegundoDam[i]
            val newModule = ModuloSQLite(item.toString(), 0.0, R.drawable.ic_menu_camera, profesor, 209,1, 2)
            SQLiteControlador.insertModulo(newModule, this)
            i+=1

        }
    }

    private fun checkDataBase(Database_path : String) : Boolean {

        var checkDB : SQLiteDatabase? = null

        try {
            checkDB = SQLiteDatabase.openDatabase(Database_path, null, SQLiteDatabase.OPEN_READONLY)
            checkDB.close()
        } catch (e : SQLiteException) {
            Log.e("Error", "No existe la base de datos " )
        }
        return checkDB != null
    }
}