package com.example.weatherapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeatherViewModel: ViewModel(){
    var weatherType: MutableLiveData<String> = MutableLiveData()
    var weatherTemperature: MutableLiveData<Double> = MutableLiveData()
    var humidity: MutableLiveData<Int> = MutableLiveData()
    var pressure: MutableLiveData<Int> = MutableLiveData()
    var windSpeed: MutableLiveData<Double> = MutableLiveData()
    private var interactor: IInteractor = Interactor()

    fun getWeatherData(name: String){
        var model = interactor.getWeatherData(name)
        weatherType.value = model.type
        weatherTemperature.value = model.temperature
        humidity.value = model.humidity
        pressure.value = model.pressure
        windSpeed.value = model.wind
    }

}