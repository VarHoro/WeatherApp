package com.example.weatherapp.domain

import androidx.lifecycle.LiveData

interface Interactor {
   fun getWeatherData(name: String): LiveData<Result<WeatherSimpleModel>>
}