package com.example.weatherapp.data

import com.example.weatherapp.domain.WeatherDataSource
import com.example.weatherapp.domain.WeatherSimpleModel

class WeatherDataSourceImpl(private val service: OpenWeatherAPI) : WeatherDataSource {
    companion object const{
        val IMAGE_URL = "https://openweathermap.org/img/wn/%s@2x.png"
    }

    override fun getData(name: String): WeatherSimpleModel {
        val call = service.getWeatherByCityName(name, APP_ID)
        val response = call.execute()
        val model = WeatherSimpleModel()
        if (response.isSuccessful) {
            val responseData = response.body()
            if (responseData != null) {
                model.apply {
                    type = responseData.weather[0].description?.capitalize()
                    wind = responseData.wind?.speed
                    icon = String.format(IMAGE_URL, responseData.weather[0].icon)
                    humidity = responseData.main?.humidity
                    pressure = responseData.main?.pressure
                    temperature = responseData.main?.temp
                }

            }
        }
        return model
    }
}