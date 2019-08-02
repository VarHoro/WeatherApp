package com.example.weatherapp.Web

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherAPI{
    @GET("/weather")
    fun getWeatherByCityName(@Query("q") query: String): Call<WeatherModel>
}