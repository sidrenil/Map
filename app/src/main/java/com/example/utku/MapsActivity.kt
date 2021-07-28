package com.example.utku

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.utku.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {

    var currentMarker: Marker? = null
    var currentLocation: Location? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null


    private lateinit var latLngList: List<LatLng>
    private lateinit var markerList: List<Marker>
    var polyLineOptions: PolylineOptions? = null
    var polyLine: Polyline? = null
    var markerOptions: MarkerOptions? = null
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    var latLng: LatLng? = null
    var marker: Marker? = null


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

        val sydney = LatLng(-33.852, 151.211)
        googleMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        val sid = LatLng(-33.545, 125.276)
        googleMap.addMarker(
            MarkerOptions()
                .position(sid)
                .title("sid")
        )
           googleMap.moveCamera(CameraUpdateFactory.newLatLng(sid))


        val polyline1 = googleMap.addPolyline(PolylineOptions()
            .clickable(true)
            .add(
                LatLng(-33.545, 125.276),
                LatLng(-33.852, 151.211),
               ))
        polyline1.tag = "sonunda"

    }
    /*
        val m1 = LatLng(20.1,40.1)
        val m2 = LatLng(30.1,50.1)
        val m3 = LatLng(40.1,60.1)
        val m4 = LatLng(50.1,70.1)
        val m5 = LatLng(60.1,80.1)

         googleMap.addMarker(MarkerOptions()
            .position(m1)
            .draggable(true)
            .title("marker")
         )



    }

     */
        /*
        mMap.setOnMapClickListener { p0 ->
            val location = LatLng(p0.latitude, p0.longitude)
            mMap.addMarker(MarkerOptions().position(location))



            binding.buttonDraw.setOnClickListener {
                if (polyLine != null) polyLine!!.remove()


                polyLineOptions = PolylineOptions()
                    .addAll(latLngList)
                    .clickable(true)

                polyLine = mMap.addPolyline(polyLineOptions)

            }
        }
    }

         */




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



/*              val sdyney = LatLng(-33.852,151.211)
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





     private fun drawMarker() {
        //val markerOption = MarkerOptions().position(latlong).title("buradasın")
        //.snippet(getAddress(latlong.latitude, latlong.latitude).toString()).draggable(true)




       // mMap.animateCamera(CameraUpdateFactory.newLatLng(latlong))
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 15f))
        // currentMarker = mMap.addMarker()
       // currentMarker?.showInfoWindow()
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