package com.example.weatherapp.model

import androidx.lifecycle.LiveData
import com.example.weatherapp.open_weather_api.WeatherModel
import kotlinx.coroutines.Job

interface IInteractor {
    fun getWeatherData(name: String): WeatherSimpleModel
}