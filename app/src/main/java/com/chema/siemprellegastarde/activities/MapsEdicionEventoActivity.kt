package com.chema.siemprellegastarde.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.chema.siemprellegastarde.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
//import com.chema.siemprellegastarde.activities.databinding.ActivityMapsEdicionEventoBinding
import com.chema.siemprellegastarde.activities.MapsEdicionEventoActivity
import com.chema.siemprellegastarde.databinding.ActivityMapsEdicionEventoBinding
import com.google.android.gms.maps.model.CircleOptions

class MapsEdicionEventoActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsEdicionEventoBinding
    private val LOCATION_REQUEST_CODE: Int = 0
    private var nombre_evento : String? = null
    private var lat_ubicacion_evento : String? = null
    private var lon_ubicacion_evento : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsEdicionEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle:Bundle? = intent.extras
        nombre_evento = (bundle?.getString("nombre_evento"))
        lat_ubicacion_evento = (bundle?.getString("lat_ubicacion_evento"))
        lon_ubicacion_evento = (bundle?.getString("lon_ubicacion_evento"))

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mMapEvento) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        val evento = LatLng(lat_ubicacion_evento.toString().toDouble(), lon_ubicacion_evento.toString().toDouble())
        val mark = mMap.addMarker(MarkerOptions().position(evento).title("${nombre_evento}"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(evento))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mark!!.getPosition(), 14F));

        mMap.setOnMyLocationClickListener(this)

        enableMyLocation()
        pintarCirculoCentro()
    }

    /**
     * función que primero compruebe si el mapa ha sido inicializado, si no es así saldrá de la función gracias
     * a la palabra return, si por el contrario map ya ha sido inicializada, es decir que el mapa ya ha cargado,
     * pues comprobaremos los permisos.
     */
    @SuppressLint("MissingPermission")
    fun enableMyLocation() {
        if (!::mMap.isInitialized) return
        if (isPermissionsGranted()) {
            mMap.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    /**
     * función que usaremos a lo largo de nuestra app para comprobar si el permiso ha sido aceptado o no.
     */
    fun isPermissionsGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    /**
     * Método que solicita los permisos.
     */
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
        }
    }


    fun pintarCirculoCentro(){
        //val markerCIFP = LatLng(38.69332,-4.10860)
        val evento = LatLng(lat_ubicacion_evento.toString().toDouble(), lon_ubicacion_evento.toString().toDouble())
        mMap.addCircle(CircleOptions().run{
            center(evento)
            radius(9.0)
            strokeColor(Color.BLUE)
            fillColor(Color.TRANSPARENT)
        })
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Boton pulsado", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "Estás en ${p0.latitude}, ${p0.longitude}", Toast.LENGTH_SHORT).show()
    }


}