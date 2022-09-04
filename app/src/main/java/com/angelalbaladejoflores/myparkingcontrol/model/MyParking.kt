package com.angelalbaladejoflores.myparkingcontrol.model

import java.io.Serializable

/*
    Autor: Ángel Albaladejo Flores
*/

class MyParking: Serializable{
    var id: Int? = null
    var date_start: String? = null
    var time_start: String? = null
    var date_end: String? = null
    var time_end: String? = null
    var location: String? = null
    var active: Int? = null
    var photo: String = "No photo"
        set(value) { field = if (value.isEmpty()) "No photo" else value }

    //Constructor to update contact info
    constructor()

    //Constructor para obtener los parkings
    constructor(id: Int, date_start: String, time_start: String, date_end: String,
                time_end: String ,location: String, active: Int, photo: String){
        this.id = id
        this.date_start = date_start
        this.time_start = time_start
        this.date_end = date_end
        this.time_end = time_end
        this.location = location
        this.active = active
        this.photo = photo
    }

    //Constructor para agregar un parking
    constructor(date_start: String, time_start: String, date_end: String,
                time_end: String ,location: String, active: Int, photo: String){
        this.date_start = date_start
        this.time_start = time_start
        this.date_end = date_end
        this.time_end = time_end
        this.location = location
        this.active = active
        this.photo = photo
    }
}