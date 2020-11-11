package android.com.diego.notasdroid.utilidades

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Base64.encodeToString
import androidx.core.net.toFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.security.KeyStore
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


object Utilidades {

    private const val ALGORITMO = "SHA-256"
    private val CHARSET = Charsets.UTF_8
    private const val ENCRIPTADO = "AES"
    private val KEYSTORE_PROVIDER = "AndroidKeyStore"
    private val KEYSTORE_ALIAS = "alias"

    /**
     * Función para opbtener el nombre del fichero
     */
    fun crearNombreFichero(): String {
        return "camara-" + UUID.randomUUID().toString() + ".jpg"
    }

    /**
     * Salva un fichero en un directorio
     */
    fun salvarImagen(path: String, nombre: String, context: Context): File? {
        // Directorio publico
        // Almacenamos en nuestro directorio de almacenamiento externo asignado en Pictures
        val dirFotos = File((context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath) + path)
        // Solo si queremos crear un directorio y quetodo sea público
        //val dirFotos = File(Environment.getExternalStorageDirectory().toString() + path)
        // Si no existe el directorio, lo creamos solo si es publico
        if (!dirFotos.exists()) {
            dirFotos.mkdirs()
        }
        try {
            val f = File(dirFotos, nombre)
            f.createNewFile()
            return f
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return null
    }

    /**
     * Comprime una imagen
     */
    fun comprimirImagen(fichero: File, bitmap: Bitmap, compresion: Int) {
        // Recuperamos el Bitmap
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compresion, bytes)
        val fo = FileOutputStream(fichero)
        fo.write(bytes.toByteArray())
        fo.close()
    }

    /**
     * Añade una imagen a la galería
     */
    fun añadirImagenGaleria(foto: Uri, nombre: String, context: Context): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "imagen")
        values.put(MediaStore.Images.Media.DISPLAY_NAME, nombre)
        values.put(MediaStore.Images.Media.DESCRIPTION, "")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.DATA, foto.toFile().absolutePath)
        return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    }

    /**
     * Elimina una imagen de la galería
     */
    fun eliminarImageGaleria(nombre: String, context: Context) {
        // Realizamos la consulta
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
        )
        val selection = "${MediaStore.Images.Media.DISPLAY_NAME} == ?"

        val selectionArgs = arrayOf(nombre)
        val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                // Borramos
                context.contentResolver.delete(contentUri, null, null);
            }
        }
    }

    /**
     * Elimina una imagen
     */
    fun eliminarImagen(imagenUri: Uri) {
        if (imagenUri.toFile().exists())
            imagenUri.toFile().delete()
    }

    /**
     * Copia un bitmap en un path determinado
     */
    fun copiarImagen(bitmap: Bitmap, path: String, compresion: Int, context: Context) {
        val nombre = crearNombreFichero()
        val fichero =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + path + File.separator + nombre
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compresion, bytes)
        val fo = FileOutputStream(fichero)
        fo.write(bytes.toByteArray())
        fo.close()
    }

    @SuppressLint("GetInstance")
    fun encriptarPwd( pwd : String) : String{

        val keygen = KeyGenerator.getInstance(ALGORITMO)
        keygen.init(256)
        val key: SecretKey = keygen.generateKey()
        val cipher = Cipher.getInstance(ENCRIPTADO)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val datesEncrypts = cipher.doFinal(pwd.toByteArray())

        return encodeToString(datesEncrypts, Base64.DEFAULT)

    }

    fun encryptString(toCipherText: String): String {

            val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
            keyStore.load(null)
            val publicKey = keyStore.getCertificate(KEYSTORE_ALIAS).publicKey

            val input = Cipher.getInstance(ENCRIPTADO)
            input.init(Cipher.ENCRYPT_MODE, publicKey)

            val outputStream = ByteArrayOutputStream()
            val cipherOutputStream = CipherOutputStream(
                outputStream, input)
            cipherOutputStream.write(toCipherText.toByteArray(CHARSET))
            cipherOutputStream.close()

            val vals = outputStream.toByteArray()

            keyStore.load(null)

            return encodeToString(vals, Base64.DEFAULT)

    }

    @SuppressLint("GetInstance")
    fun desencriptarPwd(dates : String, pwd : String) : String{

        val secretKey : SecretKeySpec  = generateKey(pwd)
        val cipher = Cipher.getInstance(ENCRIPTADO)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val datosDescodificados = Base64.decode(dates, Base64.DEFAULT)
        val datosDesencriptados = cipher.doFinal(datosDescodificados)

        return datosDesencriptados.toString()

    }

    fun encriptarPwd2(pwd: String) : String{

        val keygen = KeyGenerator.getInstance(ALGORITMO)
        keygen.init(256)
        val key: SecretKey = keygen.generateKey()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val ciphertext: ByteArray = cipher.doFinal(pwd.toByteArray())
        //val iv: ByteArray = cipher.iv

        return encodeToString(ciphertext, Base64.DEFAULT)

    }

    fun desencriptarPwd2(pwd: String) : String{

        val keygen = KeyGenerator.getInstance(ALGORITMO)
        keygen.init(256)
        val key: SecretKey = keygen.generateKey()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, key)
        val ciphertext: ByteArray = cipher.doFinal(pwd.toByteArray())
        //val iv: ByteArray = cipher.iv

        return encodeToString(ciphertext, Base64.DEFAULT)

    }

    private fun generateKey(pwd : String) : SecretKeySpec{

        val sha  = MessageDigest.getInstance(ALGORITMO)
        var key = pwd.toByteArray(CHARSET)
        key = sha.digest(key)

        return SecretKeySpec(key, ENCRIPTADO)
    }

}
