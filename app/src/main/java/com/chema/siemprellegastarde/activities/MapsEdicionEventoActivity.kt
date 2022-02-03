package com.chema.siemprellegastarde.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class MapsEdicionEventoActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsEdicionEventoBinding
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
        mMap.addMarker(MarkerOptions().position(evento).title("${nombre_evento}"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(evento))
    }
}