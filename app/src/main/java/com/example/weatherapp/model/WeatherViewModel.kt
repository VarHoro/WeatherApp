package com.example.weatherapp.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class WeatherViewModel : ViewModel() {
    var weatherType: MutableLiveData<String> = MutableLiveData()
    var weatherTemperature: MutableLiveData<Double> = MutableLiveData()
    var humidity: MutableLiveData<Int> = MutableLiveData()
    var pressure: MutableLiveData<Int> = MutableLiveData()
    var windSpeed: MutableLiveData<Double> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var interactor: IInteractor = Interactor()
    private var model: WeatherSimpleModel = WeatherSimpleModel()

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getWeatherData(name: String): Boolean {
        isLoading.value = true
        uiScope.launch{
            loadData(name)
            weatherType.value = model.type
            weatherTemperature.value = model.temperature
            humidity.value = model.humidity
            pressure.value = model.pressure
            windSpeed.value = model.wind
            isLoading.value = false
        }
        return true
    }
    
    private suspend fun loadData(name: String) = withContext(Dispatchers.Default){
        model = interactor.getWeatherData(name)
    }
}