package android.com.diego.notasdroid.ui.pruebas

import android.com.diego.notasdroid.R
import android.com.diego.notasdroid.datos.PruebaSQLite
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_prueba.view.*

class PruebasListAdapter(

    private val listaDatos: MutableList<PruebaSQLite>,
    private val listener: (PruebaSQLite) -> Unit

) : RecyclerView.Adapter<PruebasListAdapter.PruebaViewHolder>(){

    /**
     * Asociamos la vista
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PruebaViewHolder {

        return PruebaViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_prueba, parent, false)
        )
    }

    /**
     * Procesamos los datos y las metemos en un Holder
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: PruebaViewHolder, position: Int) {
        val item = listaDatos[position]
        //val imagen = Utilidades.base64ToBitmap(item.img)
        holder.txtNamePrueba.text = item.nombre
        holder.txtDatePrueba.text = item.fecha
        holder.txtNota.text = item.nota.toString()
        holder.checkRealizada.isChecked = setChecked(item.realizada)

        holder.itemView
            .setOnClickListener {
                listener(listaDatos[position])
            }

    }

    private fun setChecked(entero : Int) : Boolean{

        return entero == 1

    }

    /**
     * Elimina un item de la lista
     *
     * @param position
     */
    fun removeItem(position: Int) {
        listaDatos.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listaDatos.size)
    }

    /**
     * Recupera un Item de la lista
     *
     * @param item
     * @param position
     */
    fun restoreItem(item: PruebaSQLite, position: Int) {
        listaDatos.add(position, item)
        notifyItemInserted(position)
        notifyItemRangeChanged(position, listaDatos.size)
    }

    /**
     * Para añadir un elemento
     * @param item Dato
     */
    fun addItem(item: PruebaSQLite) {
        listaDatos.add(item)
        notifyItemInserted(listaDatos.size)
    }

    /**
     * Devuelve el número de items de la lista
     *
     * @return
     */
    override fun getItemCount(): Int {
        return listaDatos.size
    }

    /*fun list(): MutableList<ModuloSQLite> {
        return this.listaDatos
    }*/

    /**
     * Holder que encapsula los objetos a mostrar en la lista
     */
    class PruebaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Elementos graficos con los que nos asociamos
        var txtNamePrueba = itemView.txtNombrePrueba
        var txtDatePrueba = itemView.txtFechaPrueba
        var txtNota = itemView.txtNotaPrueba
        var checkRealizada: CheckBox = itemView.ckbRealizadaPrueba
    }

}

