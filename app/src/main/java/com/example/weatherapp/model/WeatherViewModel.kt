package com.example.weatherapp.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeatherViewModel : ViewModel() {
    var weatherType: MutableLiveData<String> = MutableLiveData()
    var weatherTemperature: MutableLiveData<Double> = MutableLiveData()
    var humidity: MutableLiveData<Int> = MutableLiveData()
    var pressure: MutableLiveData<Int> = MutableLiveData()
    var windSpeed: MutableLiveData<Double> = MutableLiveData()
    private var interactor: IInteractor = Interactor()

    fun getWeatherData(name: String): Boolean {
        val model = interactor.getWeatherData(name)

        return if (model.type != "") {
            weatherType.value = model.type
            weatherTemperature.value = model.temperature
            humidity.value = model.humidity
            pressure.value = model.pressure
            windSpeed.value = model.wind
            true
        } else {
            false
        }
    }

}