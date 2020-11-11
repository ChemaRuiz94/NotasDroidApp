package android.com.diego.notasdroid.datos

import android.content.Context
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import java.security.AccessControlContext

object DatosController {

    private const val DATOS_BD = "DATOS_BD_REALM"
    private const val DATOS_BD_VERSION = 1L

    fun initRealm(context: Context?){
        Realm.init(context)
        val config = RealmConfiguration.Builder()
            .name(DATOS_BD)
            .schemaVersion(DATOS_BD_VERSION)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
        Log.d("Datos", "Iniciando Realm")
    }

    fun selectDatos(): MutableList<Dato>?{
        return Realm.getDefaultInstance().copyFromRealm(
            Realm.getDefaultInstance().where<Dato>().findAll()
        )
    }

    fun insertDato(dato: Dato){
        Realm.getDefaultInstance().executeTransaction{
            it.copyToRealm(dato)
        }
    }

    fun deleteDato(dato: Dato){
        Realm.getDefaultInstance().executeTransaction{
            it.where<Dato>().equalTo("email", dato.email).findFirst()?.deleteFromRealm()
        }
    }

    fun updateDato(dato: Dato){
        Realm.getDefaultInstance().executeTransaction{
            it.copyToRealmOrUpdate(dato)
        }
    }

    fun removeAll(){
        Realm.getDefaultInstance().executeTransaction{
            it.deleteAll()
        }
    }
}