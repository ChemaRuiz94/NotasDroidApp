package android.com.diego.notasdroid.datos

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Dato (@PrimaryKey var id: Long = 0,
                 var email: String = "",
                var name: String = "",
                var imgId: String = "",
                var pwd: String = "",
                var ciclo: String = "",
                var curso:  String = "") : RealmObject() {
    constructor(email: String, name: String, imgId: String,
                pwd: String, ciclo: String, curso: String) : this((System.currentTimeMillis() / 1000L),
        email, name, imgId, pwd, ciclo, curso)
}
