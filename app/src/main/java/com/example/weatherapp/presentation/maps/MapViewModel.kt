package com.example.weatherapp.presentation.maps

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.*

class MapViewModel : ViewModel() {
    val cityName = ObservableField<String>()
    val cityCoordinates = ObservableField<String>()
    val isVisible = ObservableBoolean(false)

    fun getCityName(): String? = cityName.get()
    fun getCityCoordinates(): String? = cityCoordinates.get()

    fun setCityName(name: String) {
        cityName.set(name)
    }

    fun setCityCoordinates(coords: String) {
        cityCoordinates.set(coords)
    }

    fun setNameAndCoordinates(name: String, latlng: LatLng) {
        cityName.set(name)
        val latitude = "%.3f".format(latlng.latitude)
        val longitude = "%.3f".format(latlng.longitude)
        cityCoordinates.set(latitude.plus(", ").plus(longitude))
        isVisible.set(true)
    }

}