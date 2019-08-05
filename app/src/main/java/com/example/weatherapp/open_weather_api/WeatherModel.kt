package com.example.weatherapp.open_weather_api

import com.google.gson.annotations.SerializedName

class WeatherModel {

    @SerializedName("weather")
    var weather = ArrayList<Weather>()
    @SerializedName("main")
    var main: Main? = null
    @SerializedName("wind")
    var wind: Wind? = null
    @SerializedName("name")
    var name: String? = null
}

class Weather {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("main")
    var main: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("icon")
    var icon: String? = null
}

class Wind {
    @SerializedName("speed")
    var speed: Double = 0.0
}

class Main {
    @SerializedName("temp")
    var temp: Double = 0.0
    @SerializedName("humidity")
    var humidity: Double = 0.0
    @SerializedName("pressure")
    var pressure: Double = 0.0
}