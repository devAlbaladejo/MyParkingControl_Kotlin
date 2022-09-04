package com.angelalbaladejoflores.myparkingcontrol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.angelalbaladejoflores.myparkingcontrol.adapters.AdapterParking
import com.angelalbaladejoflores.myparkingcontrol.databinding.ActivityMainBinding
import com.angelalbaladejoflores.myparkingcontrol.model.MyParking
import com.angelalbaladejoflores.myparkingcontrol.utils.MyDBOpenHelper
import com.angelalbaladejoflores.myparkingcontrol.utils.MyUtils

/*
    Autor: Ángel Albaladejo Flores
*/

class MainActivity : AppCompatActivity(), AdapterParking.ItemLongClickListener {

    companion object {
        lateinit var parkingsDBOpenHelper: MyDBOpenHelper
    }

    private lateinit var binding: ActivityMainBinding
    private var parkings = mutableListOf<MyParking>()
    private val myAdapter = AdapterParking()

    //Implementación de la interfaz creada en el AdapterParking
    override fun onItemLongClick(view: View?, position: Int) {
        mostartPopUp(view!!, position)
    }

    //Creamos método para implementar el menu al realizar pulsación larga
    fun mostartPopUp(view: View, position: Int){
        PopupMenu(this, view).apply {
            inflate(R.menu.menu)
            setOnMenuItemClickListener {
                when(it!!.itemId){
                    //Eliminamos el parking
                    R.id.menuDelete -> {
                        val delParking = parkings.get(position)
                        if (parkingsDBOpenHelper.delParking(delParking) > 0) {
                            parkings.removeAt(position)
                            myAdapter.notifyItemRemoved(position)
                            MyUtils().showSnackBar(
                                view!!,
                                getString(R.string.snack_delete)
                            )
                        } else {
                            MyUtils().showSnackBar(
                                view!!,
                                getString(R.string.snack_delete_fail)
                            )
                        }
                        true
                    }
                    //Desaparcamos
                    R.id.menuUnpark -> {
                        val checkParking = parkings.get(position)
                        if(checkParking.active == 1){
                            checkParking.id = checkParking.id
                            checkParking.date_start = checkParking.date_start
                            checkParking.time_start = checkParking.time_start
                            checkParking.date_end = MyUtils().getDate()
                            checkParking.time_end = MyUtils().getTime()
                            checkParking.location = checkParking.location
                            checkParking.active = 0
                            checkParking.photo = checkParking.photo
                            MyDBOpenHelper.setParkingActive = true

                            parkingsDBOpenHelper.modContact(checkParking)

                            MyUtils().showSnackBar(view!!, getString(R.string.snack_unpark))

                            onResume()
                        }
                        else{
                            MyUtils().showSnackBar(view!!, getString(R.string.snack_unpark_fail))
                        }
                        true
                    }
                    else -> false
                }
            }
        }.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parkingsDBOpenHelper = MyDBOpenHelper(this, null)

        binding.add.setOnClickListener {
            val intentAddParking = Intent(this, AddParkingActivity::class.java)
            startActivity(intentAddParking)
        }

        showParkings()
    }

    //Método para mostrar todos los contactos
    fun showParkings(){
        parkings = parkingsDBOpenHelper.getAllParkings()

        myAdapter.setClickListener(this)
        myAdapter.RecyclerAdapter(parkings, this)
        binding.rvParkings.setHasFixedSize(true)
        binding.rvParkings.layoutManager = LinearLayoutManager(this)
        binding.rvParkings.adapter = myAdapter
    }

    //Se refresca el RV al volver a cargar la activity.
    override fun onResume() {
        showParkings()
        super.onResume()
    }
}