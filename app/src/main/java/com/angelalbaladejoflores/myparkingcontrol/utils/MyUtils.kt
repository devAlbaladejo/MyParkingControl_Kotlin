package com.angelalbaladejoflores.myparkingcontrol.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import com.angelalbaladejoflores.myparkingcontrol.R
import com.google.android.material.snackbar.Snackbar
import java.util.*

/*
    Autor: Ángel Albaladejo Flores
*/

class MyUtils {

    private val cal = Calendar.getInstance()

    //Método para enviar la ubicación por correo
    fun sendMail(context: Context, email: String, location: String) {
        val TO = arrayOf(email)
        val CC = arrayOf("")
        val intent = Intent(Intent.ACTION_SEND)

        intent.type = "text/"
        intent.putExtra(Intent.EXTRA_EMAIL, TO)
        intent.putExtra(Intent.EXTRA_CC, CC)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Ubicación de coche")
        intent.putExtra(Intent.EXTRA_TEXT,context.getString(R.string.text_mail, location))

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(Intent.createChooser(intent, "Enviar correo..."))
        }
    }

    //Método para visualizar la ubicación del parking
    fun openMap(context: Context, address: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${address}"))
        context.startActivity(intent)
    }

    //Método para mostrar el SnackBar
    fun showSnackBar(view: View, text: String) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
    }

    //Obtener la fecha actual
    fun getDate(): String{
        var date = ""

        var day: String = if(cal.get(Calendar.DAY_OF_MONTH) < 10) "0" + cal.get(Calendar.DAY_OF_MONTH) else "" + cal.get(
            Calendar.DAY_OF_MONTH)
        var month: String = if(cal.get(Calendar.MONTH) < 10) "0" + cal.get(Calendar.MONTH) else "" + cal.get(
            Calendar.MONTH)
        var year: String = "" + cal.get(Calendar.YEAR)

        date = "$day/$month/$year"

        return date
    }

    //Obtener la hora actual
    fun getTime(): String{
        var time = ""

        var hour: String = if(cal.get(Calendar.HOUR_OF_DAY) < 10) "0" + cal.get(Calendar.HOUR_OF_DAY) else "" + cal.get(
            Calendar.HOUR_OF_DAY)
        var minute: String = if(cal.get(Calendar.MINUTE) < 10) "0" + cal.get(Calendar.MINUTE) else "" + cal.get(
            Calendar.MINUTE)

        time = "$hour:$minute"

        return time
    }
}