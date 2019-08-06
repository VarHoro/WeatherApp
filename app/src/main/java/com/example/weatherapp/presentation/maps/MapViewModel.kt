package com.example.weatherapp.presentation.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel: ViewModel() {
    private var cityName: MutableLiveData<String> = MutableLiveData()
    private var cityCoords: MutableLiveData<String> = MutableLiveData()

    init {
        cityName.value = " "
        cityCoords.value = " "
    }

    fun getCityName():LiveData<String> = cityName
    fun getCityCoords():LiveData<String> = cityCoords

    fun setCityName(name: String){
        cityName.value = name
    }
    fun setCityCoords(coords: String){
        cityCoords.value = coords
    }

}