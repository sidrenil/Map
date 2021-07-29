package com.example.utku

import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.utku.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {


    private var currentLocation: Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var latLngList: List<LatLng>
    private lateinit var markerList: List<Marker>
    private var polyLine: Polyline? = null
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var listOfCoordinations = mutableListOf<LatLng>()
    private var counter: Int = 0
    private var blue: Int = 0
    private var yellow: Int = 0
    private var orange: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLocationPermission()


        binding.buttonClear.setOnClickListener {
            if (polyLine != null) polyLine!!.remove()
            for (marker in markerList) {
                marker.remove()
            }

            binding.seekWidth.progress = 2
            binding.seekBlue.progress = 0
            binding.seekOrange.progress = 0
            binding.seekYellow.progress = 0
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
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationPermission()
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        /*
        val sydney = LatLng(-33.852, 151.211)
        googleMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Sydney")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        val sid = LatLng(-33.545, 145.276)
        googleMap.addMarker(
            MarkerOptions()
                .position(sid)
                .title("sid")
        )
           googleMap.moveCamera(CameraUpdateFactory.newLatLng(sid))


        googleMap.addPolyline(PolylineOptions()
            .clickable(true)
            .add(sid,sydney))
        connectPolyline()


           }

         */




        mMap.setOnMapClickListener { p0 ->
            val location = LatLng(p0.latitude, p0.longitude)
            listOfCoordinations.add(location)
            mMap.addMarker(MarkerOptions().position(location))

            counter++

            if (counter >= 2) {
                googleMap.addPolyline(
                    PolylineOptions()
                        .clickable(true)
                        .add(
                            listOfCoordinations[counter - 2],
                            listOfCoordinations[counter - 1]
                        ))
                polyLine?.color = Color.rgb(255,0,0)
                setWidth()
            }

            binding.buttonDraw.setOnClickListener {
                if (polyLine != null)
                    polyLine!!.remove()


                val polylineOptions = PolylineOptions().addAll(latLngList).clickable(true)
                polyLine = mMap.addPolyline(polylineOptions)



                /*
                polyLineOptions = PolylineOptions()
                    .addAll(latLngList)
                    .clickable(true)
                 */
                //    polyLine = mMap.addPolyline(polyLineOptions)

                /*
                val polyline1 = googleMap.addPolyline(PolylineOptions()
                .clickable(true)
                .add(location))

                */
            }
        }
    }

    private fun setWidth() {
        binding.seekWidth.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val width = binding.seekWidth.progress
                if (polyLine != null)
                   polyLine!!.width = width.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }


    /*
    val sdyney = LatLng(-33.852,151.211)
    googleMap.addMarker(
    MarkerOptions()
   .position(latLng)
   .title("Marker Sydney")
   .draggable(true)
   )

  */
    /*
    binding.buttonDraw.setOnClickListener {
        if (polyLine != null) polyLine!!.remove()
        latLngList = ArrayList()
        polyLineOptions = PolylineOptions()
            .addAll(latLngList)
            .clickable(true)
        polyLine = mMap.addPolyline(PolylineOptions()
            .width(2F))

    }

     */
    //drawMarker()


    /*    val sdyney = LatLng(-33.852,151.211)
          googleMap.addMarker(
          MarkerOptions()
              .position(latLng)
              .title("Marker Sydney")
              .draggable(true)
              )
   */

    /*
    val latlong = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)
   drawMarker(latlong)

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
                  */
    /*
    val sydney = LatLng(-34.0, 151.0)
    mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
    */


    //private fun drawMarker() {
    //val markerOption = MarkerOptions().position(latlong).title("buradasın")
    //.snippet(getAddress(latlong.latitude, latlong.latitude).toString()).draggable(true)


    // mMap.animateCamera(CameraUpdateFactory.newLatLng(latlong))
    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 15f))
    // currentMarker = mMap.addMarker()
    // currentMarker?.showInfoWindow()
    //}


    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.seekBlue -> {
                blue = progress
            }
            R.id.seekOrange -> {
                orange = progress
            }
            R.id.seekYellow -> {
                yellow = progress
            }
        }
        polyLine?.color = Color.rgb(yellow, blue, orange)

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