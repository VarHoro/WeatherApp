package com.example.weatherapp.domain

interface Interactor {
   suspend fun getWeatherData(name: String): WeatherSimpleModel
}