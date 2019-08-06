package com.example.weatherapp.domain

interface WeatherDataSource{
    fun getData(name: String)
}