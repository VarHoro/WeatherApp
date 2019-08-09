package com.example.weatherapp.domain

data class WeatherSimpleModel(
    val icon: String? = null,
    val type: String? = null,
    val temperature: Double? = null,
    val pressure: Double? = null,
    val humidity: Double? = null,
    val wind: Double? = null
)