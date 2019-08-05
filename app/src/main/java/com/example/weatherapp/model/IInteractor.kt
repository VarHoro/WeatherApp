package com.example.weatherapp.model

interface IInteractor {
   suspend fun getWeatherData(name: String): WeatherSimpleModel
}