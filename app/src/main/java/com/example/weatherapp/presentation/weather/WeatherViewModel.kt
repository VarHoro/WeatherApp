package com.example.weatherapp.presentation.weather

import androidx.databinding.ObservableDouble
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.domain.Interactor
import com.example.weatherapp.domain.InteractorImpl
import com.example.weatherapp.domain.WeatherSimpleModel
import kotlinx.coroutines.*

class WeatherViewModel : ViewModel() {

    val pressure = ObservableDouble(0.0)

    var weatherType: MutableLiveData<String> = MutableLiveData()
    var weatherTemperature: MutableLiveData<Double> = MutableLiveData()
    var humidity: MutableLiveData<Double> = MutableLiveData()
    var windSpeed: MutableLiveData<Double> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var icon: MutableLiveData<String> = MutableLiveData()
    var error: MutableLiveData<String> = MutableLiveData()

    private var interactor: Interactor = InteractorImpl()
    private var model: WeatherSimpleModel = WeatherSimpleModel()

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getWeatherData(name: String): Boolean {
        isLoading.value = true
        uiScope.launch {
            loadData(name)
            if (model.error == "") {
                weatherType.value = model.type
                weatherTemperature.value = model.temperature
                humidity.value = model.humidity
                model.pressure?.let { pressure.set(it) }
                windSpeed.value = model.wind
                icon.value = model.icon
            } else {
                error.value = model.error
            }
            isLoading.value = false
        }
        return true
    }

    private suspend fun loadData(name: String) = withContext(Dispatchers.Default) {
        model = interactor.getWeatherData(name)
    }

}