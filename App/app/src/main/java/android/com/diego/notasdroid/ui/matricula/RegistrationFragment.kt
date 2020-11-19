package android.com.diego.notasdroid.ui.matricula

import android.com.diego.notasdroid.R
import android.com.diego.notasdroid.datos.ModuloSQLite
import android.com.diego.notasdroid.datos.SQLiteControlador
import android.com.diego.notasdroid.navigation.NavigationActivity
import android.com.diego.notasdroid.ui.pruebas.PruebaFragment
import android.graphics.*
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment(
    private var userSQLite: UserSQLite
) : Fragment() {
    // Mis variables
    private var modulos = mutableListOf<ModuloSQLite>() // Lista
    private lateinit var modulo : ModuloSQLite
    // Interfaz gráfica
    private lateinit var adapter: ModulosListAdapter //Adaptador de Recycler
    private lateinit var tarea: TareaCargarDatos // Tarea en segundo plano
    private var paintSweep = Paint()

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
        iniciarSwipeRecarga()

        // Cargamos los datos pro primera vez
        cargarDatos()

        // solo si hemos cargado hacemos sl swipeHorizontal
        iniciarSwipeHorizontal()

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
     * Realiza el swipe horizontal si es necesario
     */
    private fun iniciarSwipeHorizontal() {
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or
                    ItemTouchHelper.RIGHT
        ) {
            // Sobreescribimos los métodos
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Analizamos el evento según la dirección
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                // Si pulsamos a la de izquierda o a la derecha
                // Programamos la accion
                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                        eventoClicFila(modulo)
                    }
                }
            }

            // Dibujamos los botones y eveneto. Nos lo creemos :):)
            // IMPORTANTE
            // Para que no te explote las imagenes deben ser PNG
            // Así que añade un IMAGE ASEET bjándtelos de internet
            // https://material.io/resources/icons/?style=baseline
            // como PNG y cargas el de mayor calidad
            // de otra forma Bitmap no funciona bien
            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3
                    // Si es dirección a la derecha: izquierda->derecha
                    // Pintamos de azul y ponemos el icono
                    if (dX > 0) {
                        // Pintamos el botón izquierdo
                        //botonIzquierdo(canvas, dX, itemView, width)
                    } else {
                        // Caso contrario
                        botonDerecho(canvas, dX, itemView, width)
                    }
                }
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        // Añadimos los eventos al RV
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(registrationRecycler_Registration)
    }

    /**
     * Mostramos el elemento inquierdo
     * @param canvas Canvas
     * @param dX Float
     * @param itemView View
     * @param width Float
     */
    private fun botonDerecho(canvas: Canvas, dX: Float, itemView: View, width: Float) {
        // Pintamos de rojo y ponemos el icono
        paintSweep.color = Color.RED
        val background = RectF(
            itemView.right.toFloat() + dX,
            itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat()
        )
        canvas.drawRect(background, paintSweep)
        val icon: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_sweep_eliminar)
        val iconDest = RectF(
            itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right
                .toFloat() - width, itemView.bottom.toFloat() - width
        )
        canvas.drawBitmap(icon, null, iconDest, paintSweep)
    }

    /**
     * Mostramos el elemento izquierdo
     * @param canvas Canvas
     * @param dX Float
     * @param itemView View
     * @param width Float
     */
    private fun botonIzquierdo(canvas: Canvas, dX: Float, itemView: View, width: Float) {
        // Pintamos de azul y ponemos el icono
        paintSweep.color = Color.BLUE
        val background = RectF(
            itemView.left.toFloat(), itemView.top.toFloat(), dX,
            itemView.bottom.toFloat()
        )
        canvas.drawRect(background, paintSweep)
        val icon: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_sweep_detalles)
        val iconDest = RectF(
            itemView.left.toFloat() + width, itemView.top.toFloat() + width, itemView.left
                .toFloat() + 2 * width, itemView.bottom.toFloat() - width
        )
        canvas.drawBitmap(icon, null, iconDest, paintSweep)
    }

    /**
     * Carga las datos
     */
    private fun cargarDatos() {
        tarea = TareaCargarDatos()
        tarea.execute()
    }

    fun getDatosFromBD() {

        // Seleccionamos los datos
        this.modulos = SQLiteControlador.selectModulos(NavigationActivity.user.ciclo.toString(), NavigationActivity.user.curso.toString(), context)!!
    }


    /**
     * Evento cli asociado a una fila
     * @param dato ModuloSQLite
     */
    private fun eventoClicFila(dato: ModuloSQLite) {
        // Creamos el dialogo y casamos sus elementos
        //Toast.makeText(context, "PULSADO datos", Toast.LENGTH_LONG).show()
        //abrirPruebas()
        val pruebaFragment = PruebaFragment(NavigationActivity.user, dato)
        abrirPrueba(pruebaFragment)
    }

    private fun abrirPrueba(fragment: Fragment){
        val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_fade_exit,
                 R.anim.fragment_fade_enter,
                R.anim.nav_default_pop_exit_anim
            )
            transaction.replace(R.id.fragment_registration, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
    }*/

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
                Log.d("Datos", "Datos pre tamaño: " + modulos.size.toString());
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
            adapter = ModulosListAdapter(modulos) {
                eventoClicFila(it)
                modulo = it
            }

            registrationRecycler_Registration.adapter = adapter
            // Avismos que ha cambiado
            adapter.notifyDataSetChanged()
            registrationRecycler_Registration.setHasFixedSize(true)
            moduloSwipe_Registration.isRefreshing = false
            Log.d("Datos", "onPostExecute OK");
            Log.d("Datos", "Datos post tam: " + modulos.size.toString());
            Toast.makeText(context, "Datos cargados", Toast.LENGTH_LONG).show()
        }


    }
}