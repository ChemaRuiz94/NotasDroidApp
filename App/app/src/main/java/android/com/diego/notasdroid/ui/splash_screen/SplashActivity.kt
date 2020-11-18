package android.com.diego.notasdroid.ui.splash_screen

import android.com.diego.notasdroid.R
import android.com.diego.notasdroid.datos.ModuloSQLite
import android.com.diego.notasdroid.datos.SQLiteControlador
import android.com.diego.notasdroid.login.LoginActivity
import android.com.diego.notasdroid.signUp.SignUp
import android.com.diego.notasdroid.utilidades.Utilidades
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log

class SplashActivity : AppCompatActivity() {

    private var imagenesDam = arrayOf(R.drawable.acceso, R.drawable.sge)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        SystemClock.sleep(4000)

        SQLiteControlador.removeAll(this)
        initModulos()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initModulos(){
        crearModulos(R.array.primeroDamDaw, R.array.profesorPrimeroDam, 213, 1, 1)
        crearModulos(R.array.primeroDamDaw, R.array.profesorPrimeroDaw, 212, 2, 1)
        crearModulos(R.array.primeroAsir, R.array.profesorPrimeroAsir, 210, 3, 1)
        crearModulos(R.array.segundoDam, R.array.profesorSegundoDam, 209, 1, 2)
        crearModulos(R.array.segundoDaw, R.array.profesorSegundoDaw, 206, 2, 2)
        crearModulos(R.array.segundoAsir, R.array.profesorSegundoAsir, 204, 3, 2)

    }

    private fun crearModulos(idModulo : Int, idProfesor : Int, aula : Int, ciclo : Int, curso : Int){

        val listaModulo = resources.getStringArray(idModulo)
        val listaProfesores = resources.getStringArray(idProfesor)
        val bitmap: Bitmap = BitmapFactory.decodeResource(resources, imagenesDam[0])
        val imagen = Utilidades.bitmapToBase64(bitmap)!!
        var i = 0
        for (item in listaModulo){
            val profesor = listaProfesores[i]
            val newModule = ModuloSQLite(item.toString(), 0.0, R.drawable.ic_menu_camera, profesor, aula,ciclo, curso)
            SQLiteControlador.insertModulo(newModule, this)
            i+=1

        }
    }
}