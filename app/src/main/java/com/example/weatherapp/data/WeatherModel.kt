package com.example.weatherapp.data

data class WeatherModel(
    var weather: ArrayList<Weather> = ArrayList(),
    var main: Main? = null,
    var wind: Wind? = null,
    var name: String? = null
)

data class Weather(
    var id: Int = 0,
    var description: String? = null,
    var icon: String? = null,
    var main: String? = null
)

data class Wind(var speed: Double = 0.0)

data class Main(
    var temp: Double = 0.0,
    var humidity: Double = 0.0,
    var pressure: Double = 0.0
)