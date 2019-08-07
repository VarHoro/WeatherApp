package com.example.weatherapp.presentation.maps

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class MapViewModel: ViewModel() {
    val cityName = ObservableField<String>()
    val cityCoordinates = ObservableField<String>()
    val isVisible = ObservableBoolean(false)

    fun getCityName(): String? = cityName.get()
    fun getCityCoordinates():String? = cityCoordinates.get()

    fun setCityName(name: String){
        cityName.set(name)
    }
    fun setCityCoordinates(coords: String){
        cityCoordinates.set(coords)
    }

}