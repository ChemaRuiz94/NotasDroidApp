package android.com.diego.notasdroid.login

import android.com.diego.notasdroid.NavigationActivity
import android.com.diego.notasdroid.R
import android.com.diego.notasdroid.datos.UsersController
import android.com.diego.notasdroid.utilidades.Utilidades
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        updateUiWithUser()

    }

    private fun comprobarLogin(email : String, pwd :  String) : Boolean{

        var correcto = false
        val dato = UsersController.selectDatoByEmail(email)

        if (dato != null){

            correcto = pwd == dato.pwd

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

                    initMain()

                }else{ showLoginFailed()

                }

            }

        }

    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private  fun initMain(){

        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)

    }
    private fun showLoginFailed() {
        Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
    }

}