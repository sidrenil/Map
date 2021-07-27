package com.example.utku

import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.utku.databinding.ActivityMapsBinding
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import java.util.*
import java.util.function.DoubleUnaryOperator
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {

    var currentMarker: Marker? = null
    var currentLocation: Location? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null


    private lateinit var latLngList: List<LatLng>
    private lateinit var markerList: List<Marker>
    private lateinit var polyLineOptions: PolylineOptions
    var polyLine: Polyline? = null
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLocationPermission()


        binding.buttonDraw.setOnClickListener {
            if (polyLine != null) polyLine!!.remove()

            polyLineOptions.addAll(latLngList).clickable(true)
            polyLine = mMap.addPolyline(polyLineOptions)
        }

        binding.buttonClear.setOnClickListener {
            if (polyLine != null) polyLine!!.remove()
            for (marker in markerList) {
                marker.remove()
            }

            binding.seekWidth.setProgress(2)
            binding.seekBlue.setProgress(0)
            binding.seekOrange.setProgress(0)
            binding.seekYellow.setProgress(0)
        }
        latLngList = ArrayList()
        (latLngList as ArrayList<LatLng>).clear()

        markerList = ArrayList()
        (markerList as ArrayList<Marker>).clear()

        binding.seekYellow.setOnSeekBarChangeListener(this)
        binding.seekBlue.setOnSeekBarChangeListener(this)
        binding.seekOrange.setOnSeekBarChangeListener(this)
    }

    private fun getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1000
            )
            return
        }

        val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener { location ->
            if (location != null) {
                this.currentLocation = location
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)

            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationPermission()
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // val latlong = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)
        // drawMarker(latlong)

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(p0: Marker) {
            }

            override fun onMarkerDrag(p0: Marker) {
            }

            override fun onMarkerDragEnd(p0: Marker) {

                if (currentMarker != null)
                    currentMarker?.remove()

                val newLatLng = LatLng(p0.position.latitude, p0.position.latitude)
                drawMarker(newLatLng)

            }
        })
        /*
               val sydney = LatLng(-34.0, 151.0)
               mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
               mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
           }
         */


    }

    private fun drawMarker(latlong: LatLng) {
        val markerOption = MarkerOptions().position(latlong).title("buradasın")
        //.snippet(getAddress(latlong.latitude, latlong.latitude).toString()).draggable(true)

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlong))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 15f))
        currentMarker = mMap.addMarker(markerOption)
        currentMarker?.showInfoWindow()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }


/*
    private fun getAddress(lat: Double, lon: Double): String? {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val addresses = geoCoder.getFromLocation(lat, lon, 1)
        return addresses[0].getAddressLine(0)
    }

 */


}
