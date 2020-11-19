package android.com.diego.notasdroid.datos

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Prueba (@PrimaryKey var id : Long = 0,
                   var idUsuario : Long = 0,
                   var idModulo : Long = 0,
                   var fecha : Date? = null,
                   var realizada : Byte = 0,
                   var nota : Double  = 0.00) : RealmObject() {
    constructor(idUsuario: Long, idModulo: Long, fecha: Date?, realizada: Byte, nota: Double) : this ((System.currentTimeMillis() / 1000L),
        idUsuario, idModulo, fecha, realizada, nota)
}