package com.example.weatherapp.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class WeatherViewModel : ViewModel() {
    var weatherType: MutableLiveData<String> = MutableLiveData()
    var weatherTemperature: MutableLiveData<Double> = MutableLiveData()
    var humidity: MutableLiveData<Int> = MutableLiveData()
    var pressure: MutableLiveData<Int> = MutableLiveData()
    var windSpeed: MutableLiveData<Double> = MutableLiveData()
    private var interactor: IInteractor = Interactor()

    fun getWeatherData(name: String) {
        val model = interactor.getWeatherData(name)
/*
        model.observe(this, Observer { m ->
            weatherType.value = m.type
            weatherTemperature.value = m.temperature
            humidity.value = m.humidity
            pressure.value = m.pressure
            windSpeed.value = m.wind
        })*/
    }

}