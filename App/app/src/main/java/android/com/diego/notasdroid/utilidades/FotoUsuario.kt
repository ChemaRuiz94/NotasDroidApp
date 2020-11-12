package android.com.diego.notasdroid.utilidades

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toFile
import java.io.IOException

object FotoUsuario {

    // Constantes
    const val GALERIA = 1
    const val CAMARA = 2
    const val IMAGEN_DIR = "/NotesDroid"
    lateinit var IMAGEN_URI: Uri
    lateinit var IMAGEN_MEDIA_URI: Uri
    val PROPORCION = 100
    var IMAGEN_NOMBRE = ""
    var IMAGEN_COMPRES = 30

    /**
    * Elige una foto de la galeria
    */
    fun elegirFotoGaleria(actividad: Activity) {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        actividad.startActivityForResult(galleryIntent, GALERIA)
    }

    //Llamamos al intent de la camara
    fun tomarFotoCamara(applicationContext : Context, actividad : Activity) {
        // Si queremos hacer uso de fotos en aklta calidad
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        // Eso para alta o baja
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Nombre de la imagen
        IMAGEN_NOMBRE = Utilidades.crearNombreFichero()
        // Salvamos el fichero
        val fichero = Utilidades.salvarImagen(IMAGEN_DIR, IMAGEN_NOMBRE, applicationContext)!!
        IMAGEN_URI = Uri.fromFile(fichero)

        intent.putExtra(MediaStore.EXTRA_OUTPUT, IMAGEN_URI)
        // Esto para alta y baja
        actividad.startActivityForResult(intent, CAMARA)
    }


}