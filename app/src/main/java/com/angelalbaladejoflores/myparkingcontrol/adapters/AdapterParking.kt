package com.angelalbaladejoflores.myparkingcontrol.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.angelalbaladejoflores.myparkingcontrol.DetailActivity
import com.angelalbaladejoflores.myparkingcontrol.R
import com.angelalbaladejoflores.myparkingcontrol.databinding.ParkingListBinding
import com.angelalbaladejoflores.myparkingcontrol.model.MyParking

/*
    Autor: Ángel Albaladejo Flores
*/

class AdapterParking : RecyclerView.Adapter<AdapterParking.ViewHolder>() {

    private var myParkings: MutableList<MyParking> = ArrayList()
    private lateinit var context: Context
    private lateinit var longClickListener: ItemLongClickListener

    //Interfaz para eliminar el parking
    interface ItemLongClickListener{
        fun onItemLongClick(view: View?, position: Int)
    }

    // Permite capturar los eventos producidos.
    fun setClickListener(itemLongClickListener: ItemLongClickListener?) {
        longClickListener = itemLongClickListener!!
    }

    // Constructor de la clase
    fun RecyclerAdapter(myParkingsList: MutableList<MyParking>, context: Context) {
        this.myParkings = myParkingsList
        this.context = context
    }

    // Es el encargado de devolver el ViewHolder ya configurado.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ParkingListBinding.inflate(layoutInflater, parent, false).root)
    }

    // Este método se encarga de pasar los objetos, uno a uno al ViewHolder personalizado.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = myParkings.get(position)
        holder.bind(item)
    }

    // Devuelve el tamaño de la lista.
    override fun getItemCount(): Int {
        return myParkings.size
    }

    // Esta clase se encarga de rellenar cada una de las vistas que se inflarán
    // en el RecyclerView.
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        //Se usa View Binding para localizar los elementos de la lista
        private val binding = ParkingListBinding.bind(view)

        fun bind(parking: MyParking){
            binding.tvId.text = parking.id.toString()
            binding.tvId.visibility = View.GONE
            binding.tvDate.text = parking.date_start
            binding.tvTime.text = parking.time_start
            binding.tvLocation.text = context.getString(R.string.show_location_rv, parking.location)

            //Establecemos los colores según la disponibilidad del parking
            if(parking.active == 0)
                binding.cvParking.setCardBackgroundColor(Color.rgb(205,79,79))
            else
                binding.cvParking.setCardBackgroundColor(Color.rgb(79,205,79))

            //Click corto vamos a la información del parking
            itemView.setOnClickListener {
                val intentDetalle = Intent(context, DetailActivity::class.java).apply {
                    putExtra("PARKING", parking)
                }
                context.startActivity(intentDetalle)
            }

            //Click largo abrimos menu
            itemView.setOnLongClickListener {
                longClickListener.onItemLongClick(it,adapterPosition)
                true
            }
        }
    }

}