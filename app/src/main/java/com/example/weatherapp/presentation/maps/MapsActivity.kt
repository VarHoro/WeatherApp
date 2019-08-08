package com.example.weatherapp.presentation.maps

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import java.io.IOException
import com.google.android.gms.tasks.OnCompleteListener as OnCompleteListener

import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMapsBinding
import com.example.weatherapp.presentation.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_maps.*
import org.koin.android.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var mLocationPermissionGranted: Boolean = false
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private val permissionRequestCodeAccessFineLocation: Int = 1

    private lateinit var binding: ActivityMapsBinding
    private val mapViewModel: MapViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)
        binding.viewModel = mapViewModel
        binding.lifecycleOwner = this

        title = resources.getString(R.string.title_activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        mapViewModel.isVisible.set(false)

        show_weather_button.setOnClickListener {
            val intent = Intent(this, WeatherActivity::class.java)
            intent.putExtra("cityName", mapViewModel.getCityName())
            startActivity(intent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
        }
        updateLocationUI()
        getDeviceLocation()

        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it).title(getAddress(it)))
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                permissionRequestCodeAccessFineLocation
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionRequestCodeAccessFineLocation ->
                mLocationPermissionGranted = (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
        updateLocationUI()
    }

    private fun updateLocationUI() {
        try {
            if (mLocationPermissionGranted) { //show dot and button if there is permission
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("MapsActivity", "Error: ${e.message}")
        }
    }

    private fun getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) { //checking for permission
                val locationResult = mFusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener { task: Task<Location> ->
                    if (task.isSuccessful) { //show location on the map
                        val mLastKnownLocation = task.result
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLng(
                                LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation.longitude)
                            )
                        )
                    } else { //default placement
                        val sydney = LatLng(-34.0, 151.0)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("MapsActivity", "Error: ${e.message}")
        }
    }

    private fun getAddress(latlng: LatLng): String {
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText: String? = ""

        try {
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1)
            if (null != addresses && addresses.isNotEmpty()) {
                address = addresses[0]
                addressText = address.locality //trying to get city name
                if (addressText != null) { //if there is smth then show popup w/ city name and coords
                    mapViewModel.setNameAndCoordinates(addressText, latlng)
                }
            }
        } catch (e: IOException) {
            Log.e("MapsActivity", "Error: ${e.localizedMessage}")
        }
        return addressText ?: ""
    }

}
