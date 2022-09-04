package com.angelalbaladejoflores.myparkingcontrol

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.angelalbaladejoflores.myparkingcontrol.databinding.ActivityDetailBinding
import com.angelalbaladejoflores.myparkingcontrol.model.MyParking
import com.angelalbaladejoflores.myparkingcontrol.utils.MyDBOpenHelper
import com.angelalbaladejoflores.myparkingcontrol.utils.MyUtils

/*
    Autor: Ángel Albaladejo Flores
*/

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var infoParking: MyParking

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Establecemos los colores de fondo
        binding.layout.setBackgroundColor(Color.rgb(125,213,250))
        binding.ibMapInfo.setBackgroundColor(Color.rgb(125,213,250))
        binding.ibMailInfo.setBackgroundColor(Color.rgb(125,213,250))
        binding.ibCameraInfo.setBackgroundColor(Color.rgb(125,213,250))

        infoParking = intent.getSerializableExtra("PARKING") as MyParking
        setInfo()

        binding.btnUnpark.setOnClickListener {
            dialogUnpark()
        }
        binding.btnPark.setOnClickListener {
            updateParking("park")
        }
        binding.ibMapInfo.setOnClickListener{
            MyUtils().openMap(this,binding.tvLocationInfo.text.toString())
        }
        binding.ibMailInfo.setOnClickListener{
            if(binding.etMailInfo.text.isNullOrBlank())
                binding.etMailInfo.setError(getString(R.string.txt_error))
            else
                MyUtils().sendMail(this, binding.etMailInfo.text.toString(),
                    binding.tvLocationInfo.text.toString())
        }
    }

    //Método para mostrar la información del parking
    fun setInfo(){
        binding.tvDateStartInfo.text = getString(R.string.show_date_start, infoParking.date_start)
        binding.tvDateEndInfo.text = getString(R.string.show_date_end, infoParking.date_end)
        binding.tvTimeStartInfo.text = getString(R.string.show_time_start, infoParking.time_start)
        binding.tvTimeEndInfo.text = getString(R.string.show_time_end, infoParking.time_end)
        binding.tvLocationInfo.text = getString(R.string.show_location,infoParking.location)

        if(infoParking.active == 1){
            binding.btnUnpark.visibility = View.VISIBLE
            binding.btnPark.visibility = View.INVISIBLE
        }
        else if(infoParking.active == 0 && MyDBOpenHelper.setParkingActive){
            binding.btnUnpark.visibility = View.INVISIBLE
            binding.btnPark.visibility = View.VISIBLE
        }
        else{
            binding.btnUnpark.visibility = View.INVISIBLE
            binding.btnPark.visibility = View.INVISIBLE
        }

        binding.layout.setBackgroundColor(Color.rgb(125,213,250))
    }

    //Método para actualizar la información del contacto
    fun updateParking(active: String){
        val updateParking = MyParking()

        if(active.equals("park")){
            updateParking.date_start = MyUtils().getDate()
            updateParking.time_start = MyUtils().getTime()
            updateParking.date_end = "--/--/----"
            updateParking.time_end = "--:--"
            updateParking.active = 1
            MyDBOpenHelper.setParkingActive = false
        }
        else if(active.equals("unpark")){
            updateParking.date_start = infoParking.date_start
            updateParking.time_start = infoParking.time_start
            updateParking.date_end = MyUtils().getDate()
            updateParking.time_end = MyUtils().getTime()
            updateParking.active = 0
            MyDBOpenHelper.setParkingActive = true
        }

        updateParking.id = infoParking.id
        updateParking.location = infoParking.location
        updateParking.photo = updateParking.photo

        MainActivity.parkingsDBOpenHelper.modContact(updateParking)

        finish()
    }

    //Método para indicar al usuario si quiere desaparcar
    fun dialogUnpark(){
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setTitle(R.string.dialog_title_unpark)
            setMessage(R.string.dialog_text_unpark)
            setPositiveButton(R.string.dialog_yes,) { _, _ ->
                updateParking("unpark")
            }
            setNegativeButton(R.string.dialog_no, null)
        }
        builder.show()
    }

    //Controla la acción de "atrás" de la barra inferior
    override fun onBackPressed() {
        val intentMain = Intent(this, MainActivity::class.java)
        startActivity(intentMain)
    }

    //Controla la acción de "atrás" de la barra superior
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }
}