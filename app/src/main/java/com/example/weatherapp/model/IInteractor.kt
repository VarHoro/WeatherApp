package com.example.weatherapp.model

interface IInteractor {
    fun getWeatherData(name: String): WeatherSimpleModel
}