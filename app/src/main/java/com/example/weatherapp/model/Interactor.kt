package com.example.weatherapp.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.*
import java.io.IOException

import com.example.weatherapp.open_weather_api.APP_ID
import com.example.weatherapp.open_weather_api.OpenWeatherAPI

class Interactor : IInteractor {

    val BASE_URL = "https://api.openweathermap.org/"
    private var model = WeatherSimpleModel()

    override suspend fun getWeatherData(name: String): WeatherSimpleModel {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(OpenWeatherAPI::class.java)
        val call = service.getWeatherByCityName(name, APP_ID)
        val job = GlobalScope.launch {
            try {
                val response = call.execute()
                if (response.isSuccessful) {
                    val c = response.body()
                    if (c != null) {
                        model.type = c.weather[0].description?.capitalize()
                        model.wind = c.wind?.speed
                        model.icon = c.weather[0].icon
                        model.humidity = c.main?.humidity
                        model.pressure = c.main?.pressure
                        model.temperature = c.main?.temp
                    }
                } else {
                    model.error = server_error
                }
            } catch (e: IOException){
                model.error = network_fail
            }

        }
        job.join()
        return model
    }
}