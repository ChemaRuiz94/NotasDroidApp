package android.com.diego.notasdroid.datos

import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey

open class Module (@PrimaryKey var id : Long = 0,
                   var nombre : String = "",
                   var ciclo : Byte = 0,
                   var curso : Byte = 0,
                   var imagen : String = "",
                   var nota : Double = 0.0,
                   var profesor : String = "",
                   var aula : String = "") : RealmObject(){
    constructor(nombre: String, ciclo: Byte, curso: Byte, imagen: String, nota: Double, profesor: String, aula: String) :
            this((System.currentTimeMillis() / 1000L), nombre, ciclo, curso, imagen, nota, profesor, aula)
}