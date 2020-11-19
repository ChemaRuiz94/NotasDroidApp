package android.com.diego.notasdroid.ui.pruebas

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.com.diego.notasdroid.R
import android.com.diego.notasdroid.datos.ModuloSQLite
import android.com.diego.notasdroid.datos.PruebaSQLite
import android.com.diego.notasdroid.datos.SQLiteControlador
import android.com.diego.notasdroid.datos.UserSQLite
import android.graphics.*
import android.os.AsyncTask
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_layout.view.*
import kotlinx.android.synthetic.main.fragment_prueba.*

class PruebaFragment(
    private var userSQLite: UserSQLite,
    private var moduloSQLite: ModuloSQLite
) : Fragment() {

    private var pruebas = mutableListOf<PruebaSQLite>() // Lista

    // Interfaz gráfica
    private lateinit var adapter: PruebasListAdapter //Adaptador de Recycler
    private lateinit var tarea: TareaCargarDatos // Tarea en segundo plano
    private var paintSweep = Paint()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prueba, container, false)
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
        iniciarSwipeHorizontal();

        // Mostramos las vistas de listas y adaptador asociado
        pruebaRecycler.layoutManager = LinearLayoutManager(context)

        // iniciamos los eventos
        iniciarEventosBotones()
    }

    private fun iniciarEventosBotones() {
        btnPruebaNueva.setOnClickListener {
            nuevoElemento()
        }
    }


    /**
     * Iniciamos el swipe de recarga
     */
    private fun iniciarSwipeRecarga() {
        pruebaSwipe.setColorSchemeResources(R.color.colorPrimaryDark)
        //datosSwipe.setProgressBackgroundColorSchemeResource(R.color.design_default_color_primary)
        pruebaSwipe.setOnRefreshListener {
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
                    ItemTouchHelper.LEFT -> {
                        Log.d("Datos", "Tocado izquierda");
                        borrarElemento(position)
                    }
                    else -> {
                        Log.d("Datos", "Tocado derecha");
                        editarElemento(position)
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
                        botonIzquierdo(canvas, dX, itemView, width)
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
        itemTouchHelper.attachToRecyclerView(pruebaRecycler)
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
        paintSweep.setColor(Color.BLUE)
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

    /**
     * Acción primaria: Borra un elemento
     * @param position Int
     */
    private fun borrarElemento(position: Int) {
        // Acciones
        val deletedModel: PruebaSQLite = pruebas[position]
        adapter.removeItem(position)
        // Lo borramos
        SQLiteControlador.deletePrueba(deletedModel, context)
        // Mostramos la barra. Se la da opción al usuario de recuperar lo borrado con el el snackbar
        val snackbar = Snackbar.make(view!!, "Prueba eliminada", Snackbar.LENGTH_LONG)
        snackbar.setAction("DESHACER") { // undo is selected, restore the deleted item
            adapter.restoreItem(deletedModel, position)
            // Lo insertamos
            SQLiteControlador.insertPrueba(deletedModel, context)
        }
        snackbar.setActionTextColor(resources.getColor(R.color.colorPrimary))
        snackbar.show()
    }

    /**
     * Acción secundaria: Ver/Editar
     * @param position Int
     */
    private fun editarElemento(position: Int) {

        // https://inducesmile.com/android-programming/how-to-add-edittext-in-alert-dialog-programmatically-in-android/
        val editedModel: PruebaSQLite = pruebas[position]
        adapter.removeItem(position)
        // Creamos el dialogo y casamos sus elementos
        val dialogBuilder = AlertDialog.Builder(context).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_layout, null)

        //dialogView.txtNombreDialog.text = "Nuevo nombre para: " + editedModel.descripcion
        // Pulsamos cancelar
        dialogView.btnCancelarPrueba.setOnClickListener {
            dialogBuilder.dismiss()
            adapter.restoreItem(editedModel, position)
        }
        // Pulsamos aceptar
        dialogView.btnAceptarPrueba.setOnClickListener {
            // Creamos el nuevo dato
            //val datoNew = PruebaSQLite(dialogView.edtDescripcionDialog.text.toString(), editedModel.imgId)
            dialogBuilder.dismiss()
            //adapter.restoreItem(datoNew, position)
            // Actualizamos datos
            //SQLiteControlador.updatePrueba(datoNew, editedModel, context)
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }

    private fun nuevoElemento() {
        // Creamos el dialogo y casamos sus elementos
        val dialogBuilder = AlertDialog.Builder(context).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_layout, null)

        // Pulsamos cancelar
        dialogView.btnCancelarPrueba.setOnClickListener {
            dialogBuilder.dismiss()
        }
        // Pulsamos aceptar
        dialogView.btnAceptarPrueba.setOnClickListener {
            var nombre = dialogView.edtNamePrueba.text.toString()
            var fecha = dialogView.edtDatePrueba.text.toString()
            val checkBox = dialogView.ckbPruebaRealizadaDialog!!
            val entero = getChecked(checkBox)
            var texto = dialogView.edtNotaPrueba.text.toString()

            nombre = nulos(nombre)
            fecha = nulos(fecha)
            texto = nulos(texto)
            val nota = nullDouble(texto).toDouble()

            // Creamos el nuevo dato
            val datoNew = PruebaSQLite(nombre, fecha, entero, nota, userSQLite.email, moduloSQLite.nombre)
            dialogBuilder.dismiss()
            adapter.addItem(datoNew)
            // insertamos los datos
            SQLiteControlador.insertPrueba(datoNew, context)
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }
    private fun nulos( cadena : String): String {

        return if (cadena.isEmpty()) {
            ""
        }else{
            cadena
        }
    }

    private fun nullDouble(cadena: String): String {

            return if (cadena.isEmpty()){
                "0.00"
            }else{
                cadena
            }

    }

    private fun getChecked(checkBox: CheckBox): Int {

        return if (checkBox.isChecked){
            1
        }else{
            0
        }
    }

    fun getDatosFromBD() {

        // Seleccionamos los datos
        this.pruebas = SQLiteControlador.selectPruebas(userSQLite.email, moduloSQLite.nombre, context)!!
        // Si queremos le añadimos unos datos ficticios
        // this.datos.addAll(DatosController.initDatos())
    }


    /**
     * Evento cli asociado a una fila
     * @param dato Dato
     */
    private fun eventoClicFila(dato: PruebaSQLite) {
        // Creamos el dialogo y casamos sus elementos
        val dialogBuilder = AlertDialog.Builder(context).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_layout, null)

        dialogView.btnCancelarPrueba.isVisible = false
        //dialogView.edtDescripcionDialog.setText(dato.descripcion)
        //dialogView.edtDescripcionDialog.isEnabled = false
        //dialogView.txtNombreDialog.text = "Nombre: "
        // Pulsamos aceptar
        dialogView.btnAceptarPrueba.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }

    /**
     * Tarea asíncrona para la carga de datos
     */
    inner class TareaCargarDatos : AsyncTask<String?, Void?, Void?>() {
        /**
         * Acciones antes de ejecutarse
         */
        override fun onPreExecute() {
            if (pruebaSwipe.isRefreshing) {
                pruebaSwipe.isRefreshing = false
            }
            Toast.makeText(context, "Obteniendo datos", Toast.LENGTH_LONG).show()
        }

        override fun doInBackground(vararg p0: String?): Void? {
            Log.d("Datos", "Entrado en doInBackgroud");
            try {
                getDatosFromBD()
                Log.d("Datos", "Datos pre tamaño: " + pruebas.size.toString());
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
            adapter = PruebasListAdapter(pruebas) {
                eventoClicFila(it)
            }

            pruebaRecycler.adapter = adapter
            // Avismos que ha cambiado
            adapter.notifyDataSetChanged()
            pruebaRecycler.setHasFixedSize(true)
            pruebaSwipe.isRefreshing = false
            Log.d("Datos", "onPostExecute OK");
            Log.d("Datos", "Datos post tam: " + pruebas.size.toString());
            Toast.makeText(context, "Datos cargados", Toast.LENGTH_LONG).show()
        }


    }
}