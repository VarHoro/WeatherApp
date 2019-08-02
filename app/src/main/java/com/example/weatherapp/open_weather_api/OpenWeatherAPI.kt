package com.example.weatherapp.open_weather_api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherAPI{
    @GET("data/2.5/weather?")
    fun getWeatherByCityName(@Query("q") query: String, @Query("appid") app_id: String): Call<WeatherModel>
}