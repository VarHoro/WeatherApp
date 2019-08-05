package com.example.weatherapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.example.weatherapp.open_weather_api.APP_ID
import com.example.weatherapp.open_weather_api.OpenWeatherAPI

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Interactor : IInteractor {
    val BASE_URL = "https://api.openweathermap.org/"
    override fun getWeatherData(name: String): WeatherSimpleModel {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(OpenWeatherAPI::class.java)
        val call = service.getWeatherByCityName(name, APP_ID)
        val model = WeatherSimpleModel()
        GlobalScope.launch {
            val c = call.execute().body()
            if (c != null) {
                model.type = c.weather[0].description
                model.wind = c.wind?.speed
                model.icon = c.weather[0].icon
                model.humidity = c.main?.humidity
                model.pressure = c.main?.pressure
                model.temperature = c.main?.temp
            }
        }
        runBlocking {
            delay(2000L)
        }
        return model
    }
}