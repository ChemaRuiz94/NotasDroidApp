package android.com.diego.notasdroid.datos

import android.content.Context
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where

object UsersController {

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

    fun selectDatos(): MutableList<User>?{
        return Realm.getDefaultInstance().copyFromRealm(
            Realm.getDefaultInstance().where<User>().findAll()
        )
    }

    fun selectDatoByEmail(email : String) : User? {

        return Realm.getDefaultInstance().copyFromRealm(
            Realm.getDefaultInstance().where<User>().equalTo("email", email).findFirst()
        )

    }

    fun insertDato(user: User){
        Realm.getDefaultInstance().executeTransaction{
            it.copyToRealm(user)
        }
    }

    fun deleteDato(user: User){
        Realm.getDefaultInstance().executeTransaction{
            it.where<User>().equalTo("email", user.email).findFirst()?.deleteFromRealm()
        }
    }

    fun updateDato(user: User){
        Realm.getDefaultInstance().executeTransaction{
            it.copyToRealmOrUpdate(user)
        }
    }

    fun removeAll(){
        Realm.getDefaultInstance().executeTransaction{
            it.deleteAll()
        }
    }

    fun closeRealm(){

        Realm.getDefaultInstance().close()

    }
}