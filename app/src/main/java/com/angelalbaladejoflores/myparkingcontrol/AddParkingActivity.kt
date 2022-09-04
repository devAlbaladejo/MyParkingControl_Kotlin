package com.angelalbaladejoflores.myparkingcontrol

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.angelalbaladejoflores.myparkingcontrol.databinding.ActivityAddParkingBinding
import com.angelalbaladejoflores.myparkingcontrol.model.MyParking
import com.angelalbaladejoflores.myparkingcontrol.utils.MyDBOpenHelper
import com.angelalbaladejoflores.myparkingcontrol.utils.MyUtils
import java.util.*

/*
    Autor: Ángel Albaladejo Flores
*/

class AddParkingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddParkingBinding
    private var defaultLocation = "No address"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Establecemos colores de fondo
        binding.layout.setBackgroundColor(Color.rgb(125,213,250))
        binding.ibMapAdd.setBackgroundColor(Color.rgb(125,213,250))
        binding.ibCameraAdd.setBackgroundColor(Color.rgb(125,213,250))

        val actualDate = MyUtils().getDate()
        val actualTime = MyUtils().getTime()

        binding.tvDateAdd.text = actualDate
        binding.tvTimeAdd.text = actualTime

        binding.tvLocationAdd.text = defaultLocation

        binding.btnAnyadir.setOnClickListener {
            //Comprobamos que la ubicación está indicada
            if(!binding.tvLocationAdd.text.toString().equals(defaultLocation)){

                var parkingActive = 0
                var date_end = actualDate
                var time_end = actualTime

                if(MyDBOpenHelper.setParkingActive == true){
                    parkingActive = 1
                    MyDBOpenHelper.setParkingActive = false
                    date_end = "--/--/----"
                    time_end = "--:--"
                }

                val parking = MyParking(binding.tvDateAdd.text.toString(),
                    binding.tvTimeAdd.text.toString(),
                    date_end,time_end,
                    binding.tvLocationAdd.text.toString(),
                    parkingActive,
                    "")

                MainActivity.parkingsDBOpenHelper.addParking(parking)

                finish()
            }
            else{
                MyUtils().showSnackBar(it,"Debes indicar la ubicación")
            }
        }

        binding.ibMapAdd.setOnClickListener {
            val intentMain = Intent(this, MapsActivity::class.java).apply {
                putExtra("actualLocation", binding.tvLocationAdd.text.toString())
            }
            startActivityForResult(intentMain, MapsActivity.REQUEST_CODE_MAPS)
        }
    }

    //Controla la acción de "atrás" de la barra inferior
    override fun onBackPressed() {
        if(!binding.tvLocationAdd.text.toString().equals(defaultLocation)) {
            val builder = AlertDialog.Builder(this)

            builder.apply {
                setTitle(R.string.dialog_title_parking)
                setMessage(R.string.dialog_text_parking)
                setPositiveButton(R.string.dialog_yes,) { _, _ ->
                    actionButton()
                }
                setNegativeButton(R.string.dialog_no, null)
            }
            builder.show()
        }
        else
            actionButton()
    }

    //Controla la acción de "atrás" de la barra superior
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    fun actionButton(){
        val intentMain = Intent(this, MainActivity::class.java)
        startActivity(intentMain)
    }

    //Comprobamos la respuesta y mostramos la ubicación
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == MapsActivity.REQUEST_CODE_MAPS){
            if(resultCode == Activity.RESULT_OK){
                val resultado = data?.getStringExtra("location")
                binding.tvLocationAdd.text = resultado
            }
        }
    }
}