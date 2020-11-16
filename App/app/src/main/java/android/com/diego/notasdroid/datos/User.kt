package android.com.diego.notasdroid.datos

import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey

open class User (@PrimaryKey var id: Long = 0,
                 var email: String = "",
                 var name: String = "",
                 var imgId: String = "",
                 var pwd: String = "",
                 var ciclo: Byte = 0,
                 var curso: Byte = 0) : RealmObject() {
    constructor(email: String, name: String, imgId: String,
                pwd: String, ciclo: Byte, curso: Byte) : this((System.currentTimeMillis() / 1000L),
        email, name, imgId, pwd, ciclo, curso)
}
