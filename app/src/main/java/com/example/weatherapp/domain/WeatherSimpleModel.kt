package com.example.weatherapp.domain

data class WeatherSimpleModel(
    var icon: String? = null,
    var type: String? = null,
    var temperature: Double? = null,
    var pressure: Double? = null,
    var humidity: Double? = null,
    var wind: Double? = null
)