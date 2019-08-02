package com.example.weatherapp

interface IInteractor {
    fun getWeatherData(name: String): WeatherSimpleModel
}