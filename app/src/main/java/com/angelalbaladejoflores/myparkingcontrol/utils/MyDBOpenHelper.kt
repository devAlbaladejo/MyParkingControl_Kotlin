package com.angelalbaladejoflores.myparkingcontrol.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.angelalbaladejoflores.myparkingcontrol.model.MyParking

/*
    Autor: Ángel Albaladejo Flores
*/

class MyDBOpenHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){

    companion object{
        var setParkingActive: Boolean = true
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "parking.db"
        val TABLE_PARKING = "parking"
        val COLUMN_ID = "_id"
        val COLUMN_DATE_START = "date_start"
        val COLUMN_TIME_START = "time_start"
        val COLUMN_DATE_END = "date_end"
        val COLUMN_TIME_END = "time_end"
        val COLUMN_LOCATION = "location"
        val COLUMN_ACTIVE = "active"
        val COLUMN_PHOTO = "photo"
    }

    //Método para crear la BBDD
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val createDB = "CREATE TABLE $TABLE_PARKING " +
                    "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_DATE_START TEXT, $COLUMN_TIME_START TEXT," +
                    "$COLUMN_DATE_END TEXT, $COLUMN_TIME_END TEXT," +
                    "$COLUMN_LOCATION TEXT, $COLUMN_ACTIVE INTEGER," +
                    "$COLUMN_PHOTO TEXT)"
            db!!.execSQL(createDB)
        }catch(e: SQLiteException) {
            Log.e("onCreate ", e.message.toString())}
    }

    //Método para actualizar la BBDD
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            val dropDB = "DROP TABLE IF EXISTS $TABLE_PARKING"
            db!!.execSQL(dropDB)
            onCreate(db)
        }catch(e: SQLiteException) {
            Log.e("onUpgrade ", e.message.toString())}
    }

    //Método para crear un nuevo parking
    fun addParking(parking: MyParking){
        val data = ContentValues()
        data.put(COLUMN_DATE_START,parking.date_start)
        data.put(COLUMN_TIME_START,parking.time_start)
        data.put(COLUMN_DATE_END,parking.date_end)
        data.put(COLUMN_TIME_END,parking.time_end)
        data.put(COLUMN_LOCATION,parking.location)
        data.put(COLUMN_ACTIVE,parking.active)
        data.put(COLUMN_PHOTO,parking.photo)

        val db = this.writableDatabase
        db.insert(TABLE_PARKING, null, data)
        db.close()
    }

    //Método para obtener todos los parkings
    fun getAllParkings(): MutableList<MyParking>{
        var parkings = mutableListOf<MyParking>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PARKING", null)

        if(cursor.moveToFirst()){
            do{
                val parking = MyParking(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6),
                    cursor.getString(7))

                if(cursor.getInt(6) == 1)
                    setParkingActive = false

                parkings.add(parking)
            } while(cursor.moveToNext())
        }

        return parkings
    }

    //Método para eliminar un parking
    fun delParking(parking: MyParking): Int{
        val args = arrayOf(parking.id.toString())

        if (parking.active == 1)
            setParkingActive = true

        val db = this.writableDatabase
        val deleteParking = db.delete("$TABLE_PARKING", "$COLUMN_ID = ?", args)
        db.close()

        return deleteParking
    }

    //Método para modificar un contacto
    fun modContact(parking: MyParking){
        val args = arrayOf(parking.id.toString())

        val data = ContentValues()
        data.put(COLUMN_DATE_START,parking.date_start)
        data.put(COLUMN_TIME_START,parking.time_start)
        data.put(COLUMN_DATE_END,parking.date_end)
        data.put(COLUMN_TIME_END,parking.time_end)
        data.put(COLUMN_LOCATION,parking.location)
        data.put(COLUMN_ACTIVE,parking.active)
        data.put(COLUMN_PHOTO,parking.photo)

        val db = this.writableDatabase
        db.update("$TABLE_PARKING", data, "$COLUMN_ID = ?", args)
        db.close()
    }
}