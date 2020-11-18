package android.com.diego.notasdroid.login

import android.com.diego.notasdroid.R
import android.com.diego.notasdroid.datos.SQLiteControlador
import android.com.diego.notasdroid.navigation.NavigationActivity
import android.com.diego.notasdroid.utilidades.Utilidades
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var userSave = ""
    private var pwdSave = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.userSave = textUser_Login.text.toString()
        this.pwdSave = textPwd_Login.text.toString()

        updateUiWithUser()

    }

    private fun comprobarLogin(email : String, pwd :  String) : Boolean{

        var correcto = false
        //val dato = UsersController.selectDatoByEmail(email)
        val user = SQLiteControlador.selectUsuario(email, this)

        if (user != null){

            correcto = pwd == user.pwd

        }

        return correcto

    }

    private fun updateUiWithUser() {

        btnSignIn_Login.setOnClickListener {

            val email = textUser_Login.text.toString()
            val pwd = Utilidades.hashString(textPwd_Login.text.toString())

            if (email.isEmpty() or pwd.isEmpty()){

                Toast.makeText(applicationContext, R.string.action_emptyfield, Toast.LENGTH_SHORT).show()
            }else{

                if (comprobarLogin(email, pwd)){

                    Log.d("Datos", "Login con exito" )
                    initMain(email)

                }else{ showLoginFailed()

                    Log.d("Datos", "Login con exito" )
                    Toast.makeText(applicationContext, R.string.action_emptyfield, Toast.LENGTH_SHORT).show()

                }

            }

        }

    }

    private fun initMain(email: String){

        //val datos = UsersController.selectDatoByEmail(textUser_Login.text.toString())!!

        val intent = Intent(this, NavigationActivity::class.java).apply {
            putExtra("EMAIL", email)
        }
        startActivity(intent)

    }
    private fun showLoginFailed() {
        Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
    }

    // Para salvar el estado por ejemplo es usando un Bundle en el ciclo de vida
    override fun onSaveInstanceState(outState: Bundle) {
        // Salvamos en un bundle estas variables o estados de la interfaz
        outState.run {
            // Actualizamos los datos o los recogemos de la interfaz
            putString("USER", userSave)
            putString("PWD", pwdSave)
        }
        // Siempre se llama a la superclase para salvar las cosas
        super.onSaveInstanceState(outState)
    }

    // Para recuperar el estado al volver al un estado de ciclo de vida de la Interfaz
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // Recuperamos en un bundle estas variables o estados de la interfaz
        super.onRestoreInstanceState(savedInstanceState)
        // Recuperamos del Bundle
        savedInstanceState.run {
            userSave = getString("USER").toString()
            pwdSave = getString("PWD").toString()

        }
    }



}