package android.com.diego.notasdroid.utilidades

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.StrictMode
import android.provider.MediaStore
import androidx.core.app.ActivityCompat

object FotoUsuario {

    // Constantes
    private const val GALERIA = 1
    private const val CAMARA = 2
    private const val IMAGEN_DIR = "/NotesDroid"
    private lateinit var IMAGEN_URI: Uri
    private lateinit var IMAGEN_MEDIA_URI: Uri
    private val PROPORCION = 600
    private var IMAGEN_NOMBRE = ""
    private var IMAGEN_COMPRES = 30

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