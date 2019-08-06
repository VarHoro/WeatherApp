package com.example.weatherapp.data

import com.example.weatherapp.constants.APP_ID
import com.example.weatherapp.domain.WeatherDataSource

class WeatherDataSourceImpl : WeatherDataSource {
    override fun getData(name: String) {
        val call = service.getWeatherByCityName(name, APP_ID)
                val response = call.execute()
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        model.type = responseData.weather[0].description.capitalize()
                        model.wind = responseData.wind.speed
                        model.icon = responseData.weather[0].icon
                        model.humidity = responseData.main.humidity
                        model.pressure = responseData.main.pressure
                        model.temperature = responseData.main.temp
                    }
                }
    }
}