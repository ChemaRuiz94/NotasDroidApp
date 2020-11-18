package android.com.diego.notasdroid.ui.registration

import android.com.diego.notasdroid.R
import android.com.diego.notasdroid.datos.ModuloSQLite
import android.com.diego.notasdroid.utilidades.Utilidades
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_registration.view.*

class ModulosListAdapter(

    var listaDatos: MutableList<ModuloSQLite>,
    var viewHolder: ModuloViewHolder? = null,
    val listener: (ModuloSQLite) -> Unit

) : RecyclerView.Adapter<ModulosListAdapter.ModuloViewHolder>(){

    /**
     * Asociamos la vista
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuloViewHolder {
        /*return ModuloViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_registration, parent, false)
        )*/

        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_registration, parent, false)

        //viewHolder = ModuloViewHolder(vista)

        return ModuloViewHolder(vista)
    }

    /**
     * Procesamos los datos y las metemos en un Holder
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: ModuloViewHolder, position: Int) {
        val item = listaDatos[position]
        //val imagen = Utilidades.base64ToBitmap(item.img)
        holder.txtItemModulo.text = item.nombre
        holder.imgItemModulo.setImageResource(R.drawable.ic_launcher_background)
        holder.txtItemProfesor.text = item.profesor
        holder.txtItemClase.text = item.aula.toString()
        holder.txtItemNota.text = item.nota.toString()

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
    class ModuloViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Elementos graficos con los que nos asociamos
        var imgItemModulo = itemView.imgItemModulo_Registration
        var txtItemModulo = itemView.txtItemModulo_Registration
        var txtItemProfesor = itemView.txtItemModuloProfesor_Registration
        var txtItemClase = itemView.txtItemModuloClase_Registration
        var txtItemNota = itemView.txtItemModuloNota_Registration
    }

}

