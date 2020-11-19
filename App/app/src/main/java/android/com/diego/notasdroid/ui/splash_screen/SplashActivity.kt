package android.com.diego.notasdroid.ui.splash_screen

import android.com.diego.notasdroid.R
import android.com.diego.notasdroid.datos.ModuloSQLite
import android.com.diego.notasdroid.datos.SQLiteControlador
import android.com.diego.notasdroid.login.LoginActivity
import android.com.diego.notasdroid.signUp.SignUp
import android.com.diego.notasdroid.utilidades.Utilidades
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.preference.PreferenceManager
import android.util.Log

class SplashActivity : AppCompatActivity() {

    private var imagenesDam = arrayOf(R.drawable.acceso, R.drawable.sge)
    private var login = ""
    private var bbdd = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val sharedPref : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPref.apply {
            login = getString("LOGIN", "").toString()
            bbdd = getString("BBDD", "").toString()
        }
        if (login == "YES"){

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }else{
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        if (bbdd != "YES"){
            initModulos()
        }
        SystemClock.sleep(2000)

        finish()

    }

    private fun initModulos(){
        crearModulos(R.array.primeroDamDaw, R.array.profesorPrimeroDam, 213, 1, 1)
        crearModulos(R.array.primeroDamDaw, R.array.profesorPrimeroDaw, 212, 2, 1)
        crearModulos(R.array.primeroAsir, R.array.profesorPrimeroAsir, 210, 3, 1)
        crearModulos(R.array.segundoDam, R.array.profesorSegundoDam, 209, 1, 2)
        crearModulos(R.array.segundoDaw, R.array.profesorSegundoDaw, 206, 2, 2)
        crearModulos(R.array.segundoAsir, R.array.profesorSegundoAsir, 204, 3, 2)
        val sharedPref : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPref.edit()

        editor.putString("BBDD", "YES").apply()

    }

    private fun crearModulos(idModulo : Int, idProfesor : Int, aula : Int, ciclo : Int, curso : Int){

        val listaModulo = resources.getStringArray(idModulo)
        val listaProfesores = resources.getStringArray(idProfesor)
        val imagen = Uri.parse(R.drawable.sge.toString())
        val img = imagen.toString().toInt()
        var i = 0
        for (item in listaModulo){
            val profesor = listaProfesores[i]
            val newModule = ModuloSQLite(item.toString(), 0.0, img, profesor, aula,ciclo, curso)
            SQLiteControlador.insertModulo(newModule, this)
            i+=1

        }
    }
}