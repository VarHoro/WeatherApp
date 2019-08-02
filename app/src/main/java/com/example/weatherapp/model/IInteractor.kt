package com.example.weatherapp.model

import androidx.lifecycle.LiveData

interface IInteractor {
    fun getWeatherData(name: String): LiveData<WeatherSimpleModel>
}