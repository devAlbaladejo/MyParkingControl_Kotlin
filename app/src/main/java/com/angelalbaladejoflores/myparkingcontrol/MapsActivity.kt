package com.angelalbaladejoflores.myparkingcontrol

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.angelalbaladejoflores.myparkingcontrol.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker

/*
    Autor: Ángel Albaladejo Flores
*/

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
        var REQUEST_CODE_MAPS = 456
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocation: FusedLocationProviderClient
    private var defaultLocation = "No address"
    private var location: Marker? = null
    private lateinit var locationActual: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Solicitamos permisos sobre la ubicación actual
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        val actualLocation = intent.getStringExtra("actualLocation")

        fusedLocation.lastLocation.addOnSuccessListener {
            if(it != null){
                if(actualLocation.equals(defaultLocation)){
                    val firstlocation = LatLng(it.latitude, it.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstlocation,12f))
                    getLocation(firstlocation)
                }
                else{
                    val savedLocation = actualLocation!!.split(" ")
                    val latLng = LatLng(savedLocation[0].toDouble(),savedLocation[1].toDouble())
                    getLocation(latLng)
                }
            }
        }
    }

    //Obtenemos la localización
    fun getLocation(latLng: LatLng){
        val markerOptions = MarkerOptions().position(latLng)
        markerOptions.icon(
            BitmapDescriptorFactory.defaultMarker(
            BitmapDescriptorFactory.HUE_ORANGE
        ))

        locationActual = "" + latLng.latitude + " " + latLng.longitude
        markerOptions.title(locationActual)

        location = mMap.addMarker(markerOptions)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

        val builder = AlertDialog.Builder(this)

        //Indicamos si quiere guardar la ubicación actual
        builder.apply {
            setTitle(R.string.dialog_title_map)
            setMessage(R.string.dialog_text_map)
            setPositiveButton(R.string.dialog_yes) { _, _ ->
                val intentResultado = Intent().apply {
                    putExtra("location", locationActual)
                }
                setResult(Activity.RESULT_OK, intentResultado)
                finish()
            }
            setNegativeButton(R.string.dialog_no){ _, _ ->
                actionButton()
            }
        }
        builder.show()
    }

    //Controla la acción de "atrás" de la barra inferior
    override fun onBackPressed() {
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
}