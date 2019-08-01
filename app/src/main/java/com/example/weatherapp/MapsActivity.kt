package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import java.io.IOException
import com.google.android.gms.tasks.OnCompleteListener as OnCompleteListener

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var mLocationPermissionGranted: Boolean = false
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private val PERMISSION_REQUEST_ACCES_FINE_LOCATION = 1
    private lateinit var popupView: LinearLayout
    private lateinit var cityName: TextView
    private lateinit var cityCoordinates: TextView
    private val vm: MapViewModel by lazy {
        ViewModelProviders.of(this).get(MapViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        popupView = findViewById(R.id.popup_view)
        popupView.visibility = GONE
        cityName = findViewById(R.id.city_name)
        cityCoordinates = findViewById(R.id.city_coordinates)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ContextCompat.checkSelfPermission(this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true
        }
        updateLocationUI()
        getDeviceLocation()

        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it).title(getAddress(it)))
        }
    }

    private fun getLocationPermission(){
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCES_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            PERMISSION_REQUEST_ACCES_FINE_LOCATION ->
                mLocationPermissionGranted = (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
        updateLocationUI()
    }

    private fun updateLocationUI(){
        try{
            if (mLocationPermissionGranted){
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                getLocationPermission()
            }
        } catch (e: SecurityException){
            Log.e("Exception: %s", e.message)
        }
    }

    private fun getDeviceLocation(){
        try {
            if (mLocationPermissionGranted){
                var locationResult = mFusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener {task: Task<Location> ->
                    if (task.isSuccessful){
                        var mLastKnownLocation = task.result
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(
                            LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation.longitude)))
                    } else {
                        val sydney = LatLng(-34.0, 151.0)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException){
            Log.e("Exception: %s", e.message)
        }
    }

    private fun getAddress(latlng: LatLng): String{
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText:String? = ""

        try{
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1)
            if (null != addresses && addresses.isNotEmpty()){
                address = addresses[0]
                addressText = address.locality
                if (addressText != null){
                    popupView.visibility = VISIBLE
                    cityName.text = addressText
                    val latitude = latlng.latitude.toString()
                    val longitude = latlng.longitude.toString()
                    cityCoordinates.text = latitude.plus(", ").plus(longitude)
                }
            }
        } catch (e: IOException){
            Log.e("MapsActivity", e.localizedMessage)
        }
        return addressText ?: ""
    }
}
