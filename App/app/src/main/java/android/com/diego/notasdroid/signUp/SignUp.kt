package android.com.diego.notasdroid.signUp

import android.app.AlertDialog
import android.com.diego.notasdroid.R
import android.com.diego.notasdroid.datos.Dato
import android.com.diego.notasdroid.datos.DatosController
import android.com.diego.notasdroid.ui.login.LoginActivity
import android.com.diego.notasdroid.utilidades.FotoUsuario
import android.com.diego.notasdroid.utilidades.Utilidades
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.IOException


class SignUp : AppCompatActivity() {

    private lateinit var nameRegistro : String
    private lateinit var emailRegistro : String
    private lateinit var pwdRegistro : String
    private lateinit var ciclo : String
    private lateinit var curso : String
    private var datos = mutableListOf<Dato>()
    // Constantes
    private val GALERIA = 1
    private val CAMARA = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initUI()
        elegirFoto()
        registrarDatos()
        //getDatosFromBD()
        //DatosController.removeAll()

    }

    private fun initUI(){

        DatosController.initRealm(this)

        initSpinnerCurso()

        initSpinnerCiclo()

        //imgBtnFotoPerfil_SignUp.setBackgroundResource(R.drawable.ic_userdefault)

    }

    fun getDatosFromBD() {

        this.datos = DatosController.selectDatos()!!
        //Toast.makeText(this, datos[1].pwd, Toast.LENGTH_SHORT).show()

    }

    private fun initID() : Long {

        this.datos = DatosController.selectDatos()!!
        var id : Long = 0

        if (datos.lastIndex != -1){

            val ultimo = datos.lastIndex
            id = if (datos[ultimo].id > (-1).toLong()) { datos[ultimo].id + 1 }
            else{ 0 }
        }

        return id

    }

    private fun registrarDatos(){

        btnSignUp_SignUp.setOnClickListener {

            nameRegistro = txtName_SignUp.text.toString()
            emailRegistro = textUser_SignUp.text.toString()
            pwdRegistro = textPwd_SignUp.text.toString()
            ciclo = spnCycle_SignUp.selectedItem.toString()
            curso = spnYear_SignUp.selectedItem.toString()

            val pwdEncriptada = Utilidades.hashString(pwdRegistro)

            if (comprobarCamposCompletos(emailRegistro, pwdRegistro, nameRegistro)){

                val id = initID()
                val newDato = Dato(
                    id,
                    emailRegistro,
                    nameRegistro,
                    imgUserPhoto_SignUp.toString(),
                    pwdEncriptada,
                    ciclo,
                    curso
                )
                DatosController.insertDato(newDato)
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
                    foto
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
                // Esta línea para baja calidad
                //thumbnail = (Bitmap) data.getExtras().get("data");
                // Esto para alta
                //val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, IMAGEN_URI)
                //val foto: Bitmap = ImageDecoder.decodeBitmap(source)

                val foto: Bitmap

                if (Build.VERSION.SDK_INT < 28) {
                    foto = MediaStore.Images.Media.getBitmap(contentResolver, FotoUsuario.IMAGEN_URI)
                } else {
                    val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, FotoUsuario.IMAGEN_URI)
                    foto = ImageDecoder.decodeBitmap(source)
                }

                // Vamos a probar a comprimir
                //FotoUsuario.IMAGEN_COMPRES = mainSeekCompresion.progress * 10
                //Utilidades.comprimirImagen(FotoUsuario.IMAGEN_URI.toFile(), foto, FotoUsuario.IMAGEN_COMPRES)

                // Si estamos en módo publico la añadimos en la biblioteca
                /*if (PUBLICO) {
                    // Por su queemos guardar el URI con la que se almacena en la Mediastore
                    FotoUsuario.IMAGEN_MEDIA_URI = Utilidades.añadirImagenGaleria(
                        FotoUsuario.IMAGEN_URI,
                        FotoUsuario.IMAGEN_NOMBRE, applicationContext)!!
                }*/

                // La adaptamos al imageview y la mostramos
                val viewWidthToBitmapWidthRatio = imgUserPhoto_SignUp.width.toDouble() / foto.width.toDouble()
                imgUserPhoto_SignUp.layoutParams.height = ((foto.height * viewWidthToBitmapWidthRatio).toInt())
                imgUserPhoto_SignUp.setImageBitmap(foto)
                //mainTvPath.text = FotoUsuario.IMAGEN_URI.toString()

                Toast.makeText(this, "¡Foto Salvada!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "¡Fallo Camara!", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
