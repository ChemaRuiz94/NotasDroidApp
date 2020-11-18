package android.com.diego.notasdroid.ui.registration

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.com.diego.notasdroid.R
import android.com.diego.notasdroid.datos.ModuloSQLite
import android.com.diego.notasdroid.datos.SQLiteControlador
import android.com.diego.notasdroid.navigation.NavigationActivity
import android.graphics.*
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    // Interfaz gráfica
    //private lateinit var adapter: ModulosListAdapter
    private var adapter: RecyclerView.Adapter<ModulosListAdapter.ModuloViewHolder>? = null
    //private lateinit var navigationActivity : NavigationActivity
    private lateinit var tarea: TareaCargarDatos // Tarea en segundo plano
    private var lista : RecyclerView? = null
    private var layoutManager : RecyclerView.LayoutManager? = null
    private var datos = mutableListOf<ModuloSQLite>()
    private var paintSweep = Paint()
    //private var user = navigationActivity.user

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Iniciamos la interfaz
        initUI()

    }

    private fun initUI() {

        // iniciamos el swipe para recargar
        iniciarSwipeRecarga();

        // Cargamos los datos pro primera vez
        cargarDatos()

        // solo si hemos cargado hacemos sl swipeHorizontal
        //iniciarSwipeHorizontal();

        // Mostramos las vistas de listas y adaptador asociado
        registrationRecycler_Registration.layoutManager = LinearLayoutManager(context)

        // iniciamos los eventos
        //iniciarEventosBotones()
    }

    /*private fun iniciarEventosBotones() {
        datosBtnNuevo.setOnClickListener {
            nuevoElemento()
        }
    }*/


    /**
     * Iniciamos el swipe de recarga
     */
    private fun iniciarSwipeRecarga() {
        moduloSwipe_Registration.setColorSchemeResources(R.color.colorPrimaryDark)
        //datosSwipe.setProgressBackgroundColorSchemeResource(R.color.design_default_color_primary)
        moduloSwipe_Registration.setOnRefreshListener {
            cargarDatos()
        }
    }

    /**
     * Carga las datos
     */
    private fun cargarDatos() {
        tarea = TareaCargarDatos()
        tarea.execute()
    }

    fun getDatosFromBD() {
        // Vamos a borralo todo, opcional
        // DatosController.removeAll(context)
        // insertamos un dato
        //SQLiteControlador.insertModulo(Dato("Dato 1", android.R.drawable.ic_dialog_email), context)
        // Seleccionamos los datos
        this.datos = SQLiteControlador.selectModulos("1", "2", context)!!
        // Si queremos le añadimos unos datos ficticios
        // this.datos.addAll(DatosController.initDatos())
    }


    /**
     * Evento cli asociado a una fila
     * @param dato Dato
     */
    private fun eventoClicFila(dato: ModuloSQLite) {
        // Creamos el dialogo y casamos sus elementos
        val dialogBuilder = AlertDialog.Builder(context).create()
        val inflater = this.layoutInflater
        if((activity as NavigationActivity?)!!.isClicEventoFila) {
            Log.d("Noticias", "Has hecho clic en la noticia: ")
            //abrirNoticia(noticia)
        }
        /*val dialogView = inflater.inflate(R.layout.dialog_layout, null)

        dialogView.btnCancelarDialog.isVisible = false
        dialogView.edtDescripcionDialog.setText(dato.descripcion)
        dialogView.edtDescripcionDialog.isEnabled = false
        dialogView.txtNombreDialog.text = "Nombre: "
        // Pulsamos aceptar
        dialogView.btnAceptarDialog.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.show()*/
    }

    /**
     * Tarea asíncrona para la carga de datos
     */
    inner class TareaCargarDatos : AsyncTask<String?, Void?, Void?>() {
        /**
         * Acciones antes de ejecutarse
         */
        override fun onPreExecute() {
            if (moduloSwipe_Registration.isRefreshing) {
                moduloSwipe_Registration.isRefreshing = false
            }
            Toast.makeText(context, "Obteniendo datos", Toast.LENGTH_LONG).show()
        }

        override fun doInBackground(vararg p0: String?): Void? {
            Log.d("Datos", "Entrado en doInBackgroud");
            try {
                getDatosFromBD()
                Log.d("Datos", "Datos pre tamaño: " + datos.size.toString());
            } catch (e: Exception) {
                Log.e("T2Plano ", e.message.toString());
            }
            Log.d("Datos", "onDoInBackgroud OK");
            return null
        }

        /**
         * Procedimiento a realizar al terminar
         * Cargamos la lista
         *
         * @param args
         */
        override fun onPostExecute(args: Void?) {
            Log.d("Datos", "entrando en onPostExecute")
            adapter = ModulosListAdapter(datos) {
                eventoClicFila(it)
            }

            registrationRecycler_Registration.adapter = adapter
            // Avismos que ha cambiado
            (adapter as ModulosListAdapter).notifyDataSetChanged()
            registrationRecycler_Registration.setHasFixedSize(true)
            moduloSwipe_Registration.isRefreshing = false
            Log.d("Datos", "onPostExecute OK");
            Log.d("Datos", "Datos post tam: " + datos.size.toString());
            Toast.makeText(context, "Datos cargados", Toast.LENGTH_LONG).show()
        }


    }}
