package android.com.diego.notasdroid.signUp

import android.app.AlertDialog
import android.com.diego.notasdroid.R
import android.com.diego.notasdroid.datos.SQLiteControlador
import android.com.diego.notasdroid.datos.User
import android.com.diego.notasdroid.datos.UserSQLite
import android.com.diego.notasdroid.datos.UsersController
import android.com.diego.notasdroid.login.LoginActivity
import android.com.diego.notasdroid.utilidades.FotoUsuario
import android.com.diego.notasdroid.utilidades.Utilidades
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.IOException


class SignUp : AppCompatActivity() {

    private var nameSave = ""
    private var emailSave = ""
    private var pwdSave = ""
    private var cicloSave : Spinner? = null
    private var cursoSave : Spinner? = null
    private var imagenSave : Bitmap? = null
    private var datos = mutableListOf<User>()
    // Constantes
    private val GALERIA = 1
    private val CAMARA = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initUI()
        //getDatosFromBD()
        //DatosController.removeAll()

    }

    private fun initUI(){

        //UsersController.initRealm(this)

        initSpinnerCurso()

        initSpinnerCiclo()

        elegirFoto()

        registrarDatos()

        initSaveDatos()

    }

    private fun initSaveDatos(){

        this.nameSave = txtName_SignUp.text.toString()
        this.emailSave = textUser_SignUp.text.toString()
        this.pwdSave = textPwd_SignUp.text.toString()
        this.imagenSave = imgUserPhoto_SignUp.drawable.toBitmap()
        this.cicloSave = spnCycle_SignUp
        this.cursoSave = spnYear_SignUp

    }

    private fun registrarDatos(){

        btnSignUp_SignUp.setOnClickListener {

            val nameRegistro = txtName_SignUp.text.toString()
            val emailRegistro = textUser_SignUp.text.toString()
            val pwdRegistro = textPwd_SignUp.text.toString()
            val ciclo = spnCycle_SignUp.selectedItemPosition
            val curso = spnYear_SignUp.selectedItemPosition
            val imagen = Utilidades.bitmapToBase64(imgUserPhoto_SignUp.drawable.toBitmap())!!

            val pwdEncriptada = Utilidades.hashString(pwdRegistro)

            if (comprobarCamposCompletos(emailRegistro, pwdRegistro, nameRegistro)){

                /*val newDato = User(
                    emailRegistro,
                    nameRegistro,
                    imagen,
                    pwdEncriptada,
                    ciclo,
                    curso
                )
                UsersController.insertDato(newDato)*/

                val newUser = UserSQLite(emailRegistro, nameRegistro, imagen, pwdEncriptada, ciclo, curso)
                SQLiteControlador.insertUsuario(newUser, this)
                initLogin()
            }

        }
    }

    private fun initLogin(){

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }

    private fun elegirFoto(){

        imgUserPhoto_SignUp.setOnClickListener {
            initDialogFoto()
        }

    }

    private fun comprobarCamposCompletos(email: String, pwd: String, name: String): Boolean {

        var correcto : Boolean
        correcto = false

        if (!fieldEmpty(email, pwd, name) or !comprobarSpinners(spnCycle_SignUp)
                or !comprobarSpinners(spnYear_SignUp)){
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

    /**
     * Muestra el diálogo para tomar foto o elegir de la galería
     */
    private fun initDialogFoto() {
        val fotoDialogoItems = arrayOf(
            "Seleccionar fotografía de galería",
            "Capturar fotografía desde la cámara"
        )
        // Creamos el dialog con su builder
        AlertDialog.Builder(this)
            .setTitle("Seleccionar Acción")
            .setItems(fotoDialogoItems) { dialog, modo ->
                when (modo) {
                    0 -> FotoUsuario.elegirFotoGaleria(this)
                    1 -> FotoUsuario.tomarFotoCamara(applicationContext, this)
                }
            }
            .show()
    }

    private fun initSpinnerCurso(){

        val years : ArrayList<String> = ArrayList(3)

        years.add(getString(R.string.prompt_year))
        years.add(getString(R.string.spinner_firstYear_Registration))
        years.add(getString(R.string.spinner_secondYear_Registration))

        val adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, years)
        this.spnYear_SignUp.adapter = adapter

    }

    private fun initSpinnerCiclo(){

        val cycles : ArrayList<String> = ArrayList()

        cycles.add(getString(R.string.prompt_cycle))
        cycles.add(getString(R.string.spinner_dam_registration))
        cycles.add(getString(R.string.spinner_daw_registration))
        cycles.add(getString(R.string.spinner_asir_registration))

        val adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cycles)
        this.spnCycle_SignUp.adapter = adapter

    }

    private fun comprobarSpinners(spinner: Spinner) : Boolean{

        var correct = false

        if (spinner.selectedItemPosition != 0){ correct = true }

        return correct

    }


    /**
     * Siempre se ejecuta al realizar una acción
     * @param requestCode Int
     * @param resultCode Int
     * @param data Intent?
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("FOTO", "Opción::--->$requestCode")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_CANCELED) {
            Log.d("FOTO", "Se ha cancelado")
        }
        if (requestCode == FotoUsuario.GALERIA) {
            Log.d("FOTO", "Entramos en Galería")
            if (data != null) {
                // Obtenemos su URI con su dirección temporal
                val contentURI = data.data!!
                try {
                    // Obtenemos el bitmap de su almacenamiento externo
                    // Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    val bitmap: Bitmap
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, contentURI);
                    } else {
                        val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, contentURI)
                        bitmap = ImageDecoder.decodeBitmap(source)
                    }
                    // Para jugar con las proporciones y ahorrar en memoria no cargando toda la foto, solo carga 600px max
                    val prop = FotoUsuario.PROPORCION / bitmap.width.toFloat()
                    // Actualizamos el bitmap para ese tamaño, luego podríamos reducir su calidad
                    val foto = Bitmap.createScaledBitmap(
                        bitmap,
                        FotoUsuario.PROPORCION, (bitmap.height * prop).toInt(), false
                    )
                    Toast.makeText(this, "¡Foto rescatada de la galería!", Toast.LENGTH_SHORT).show()
                    // La adaptamos al imageview y la mostramos
                    val viewWidthToBitmapWidthRatio = imgUserPhoto_SignUp.width.toDouble() / foto.width.toDouble()
                    imgUserPhoto_SignUp.layoutParams.height = ((foto.height * viewWidthToBitmapWidthRatio).toInt())
                    imgUserPhoto_SignUp.setImageBitmap(foto)
                    // Vamos a compiar nuestra imagen en nuestro directorio
                    Utilidades.copiarImagen(
                        bitmap,
                        FotoUsuario.IMAGEN_DIR,
                        FotoUsuario.IMAGEN_COMPRES, applicationContext
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "¡Fallo Galeria!", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == FotoUsuario.CAMARA) {
            Log.d("FOTO", "Entramos en Camara")
            // Cogemos la imagen, pero podemos coger la imagen o su modo en baja calidad (thumbnail)
            try {

                val foto: Bitmap

                if (Build.VERSION.SDK_INT < 28) {
                    foto = MediaStore.Images.Media.getBitmap(contentResolver, FotoUsuario.IMAGEN_URI)
                } else {
                    val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, FotoUsuario.IMAGEN_URI)
                    foto = ImageDecoder.decodeBitmap(source)
                }

                // La adaptamos al imageview y la mostramos
                val viewWidthToBitmapWidthRatio = imgUserPhoto_SignUp.width.toDouble() / foto.width.toDouble()
                imgUserPhoto_SignUp.layoutParams.height = ((foto.height * viewWidthToBitmapWidthRatio).toInt())
                imgUserPhoto_SignUp.setImageBitmap(foto)

                Toast.makeText(this, "¡Foto Salvada!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "¡Fallo Camara!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Para salvar el estado por ejemplo es usando un Bundle en el ciclo de vida
    override fun onSaveInstanceState(outState: Bundle) {
        // Salvamos en un bundle estas variables o estados de la interfaz
        outState.run {
            // Actualizamos los datos o los recogemos de la interfaz
            putString("EMAIL", emailSave)
            putString("NOMBRE", nameSave)
            putString("IMAGEN", imagenSave?.let { Utilidades.bitmapToBase64(it) })
            cicloSave?.selectedItemPosition?.let { putInt("CICLO", it) }
            cursoSave?.selectedItemPosition?.let { putInt("CURSO", it) }
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
            emailSave = getString("EMAIL").toString()
            nameSave = getString("NOMBRE").toString()
            imagenSave = Utilidades.base64ToBitmap(getString("IMAGEN").toString())
            cicloSave?.setSelection(getInt("CICLO"))
            cursoSave?.setSelection(getInt("CURSO"))
            pwdSave = getString("PWD").toString()

        }
    }

}
