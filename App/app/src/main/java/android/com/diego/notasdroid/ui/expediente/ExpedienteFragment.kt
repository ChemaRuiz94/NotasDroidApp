package android.com.diego.notasdroid.ui.expediente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.com.diego.notasdroid.R
import android.com.diego.notasdroid.datos.SQLiteControlador
import android.com.diego.notasdroid.navigation.NavigationActivity
import android.com.diego.notasdroid.utilidades.Utilidades
import kotlinx.android.synthetic.main.fragment_expediente.*

class ExpedienteFragment : Fragment() {

    private var notaMedia : Double = 0.0
    private var suma : Double = 0.0
    private val user = NavigationActivity.user

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expediente, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Iniciamos la interfaz
        init()

    }

    private fun init(){

        mostrarDatos()

    }

    private fun mostrarDatos(){

        val bitmap = Utilidades.base64ToBitmap(user.img)
        notaMedia = calcularNotaMedia()

        imgUserExpediente.setImageBitmap(bitmap)
        this.textNameUserExpediente.text = user.name
        this.textCicloUserExpediente.text = user.ciclo.toString()
        this.textEmailUserExpediente.text = user.email
        this.textNotaUserExpediente.text = String.format("%.2f",notaMedia)
    }

    private fun calcularNotaMedia(): Double {

        val modulos = SQLiteControlador.selectModulos(user.ciclo.toString(),
            user.curso.toString(), context)

        if (modulos != null) {

            for (modulo in modulos){ suma += modulo.nota }

        }

        return suma/modulos!!.size
    }
}