package com.example.weatherapp.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.example.weatherapp.open_weather_api.APP_ID
import com.example.weatherapp.open_weather_api.OpenWeatherAPI
import kotlinx.coroutines.*

class Interactor : IInteractor {

    val BASE_URL = "https://api.openweathermap.org/"
    private var model = WeatherSimpleModel()

    override fun getWeatherData(name: String): WeatherSimpleModel {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(OpenWeatherAPI::class.java)
        val call = service.getWeatherByCityName(name, APP_ID)
        GlobalScope.launch {
            val c = call.execute().body()
            if (c != null) {
                model.type = c.weather[0].description?.capitalize()
                model.wind = c.wind?.speed
                model.icon = c.weather[0].icon
                model.humidity = c.main?.humidity
                model.pressure = c.main?.pressure
                model.temperature = c.main?.temp
            }
        }
        runBlocking { delay(3000L) }
        return model
    }
}