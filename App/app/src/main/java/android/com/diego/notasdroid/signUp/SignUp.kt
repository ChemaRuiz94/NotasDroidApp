package android.com.diego.notasdroid.signUp

import android.com.diego.notasdroid.R
import android.com.diego.notasdroid.datos.Dato
import android.com.diego.notasdroid.datos.DatosController
import android.com.diego.notasdroid.ui.login.LoginFormState
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUp : AppCompatActivity() {

    private lateinit var nameRegistro : String
    private lateinit var emailRegistro : String
    private lateinit var pwdRegistro : String
    private lateinit var ciclo : String
    private lateinit var curso : String
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm
    private var datos = mutableListOf<Dato>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initUI()

        registrarDatos()

    }

    private fun initUI(){

        DatosController.initRealm(this)
    }


    private fun registrarDatos(){

        val button = findViewById<Button>(R.id.btnSignUp_SignUp)
        button.setOnClickListener {

            nameRegistro = txtName_SignUp.text.toString()
            emailRegistro = textUser_SignUp.text.toString()
            pwdRegistro = textPwd_SignUp.text.toString()
            //ciclo = spnCycle_SignUp.selectedItem.toString()
            //curso = spnYear_SignUp.selectedItem.toString()

            if (comprobarCamposCompletos(emailRegistro, pwdRegistro, nameRegistro)){

                val newDato = Dato(0,emailRegistro, nameRegistro, 0, pwdRegistro, "", "")
                DatosController.insertDato(newDato)
                Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun comprobarCamposCompletos(email : String, pwd : String, name : String): Boolean {

        var correcto : Boolean
        correcto = false

        if (!fieldEmpty(email, pwd, name)){
            Toast.makeText(this, R.string.action_emptyfield, Toast.LENGTH_SHORT).show()
            //_loginForm.value = LoginFormState(usernameError = R.string.action_emptyfield)
        }
        else{

            if (!isEmailValid(email)){
                Toast.makeText(this, R.string.invalid_username, Toast.LENGTH_SHORT).show()
                //_loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
            }else if (!isPasswordValid(pwd)) {
                Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_SHORT).show()
                //_loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
            }else{
                correcto = true
            }

        }
        return correcto
    }

    private fun fieldEmpty(email: String, pwd: String, name: String) : Boolean{

        return !(name.isEmpty() or email.isEmpty() or
                pwd.isEmpty())
    }

    // A placeholder username validation check
    private fun isEmailValid(emailUser: String): Boolean {
        return if (emailUser.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()
        } else {
            emailUser.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}