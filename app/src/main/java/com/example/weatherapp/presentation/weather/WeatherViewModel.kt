package com.example.weatherapp.presentation.weather

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.domain.Interactor
import com.example.weatherapp.domain.WeatherSimpleModel
import kotlinx.coroutines.*

class WeatherViewModel(private val interactor: Interactor) : ViewModel() {

    val pressure = ObservableDouble(0.0)
    val pressureExists = ObservableBoolean(true)
    val weatherTemperature = ObservableDouble(0.0)
    val temperatureExists = ObservableBoolean(true)
    val humidity = ObservableDouble(0.0)
    val humidityExists = ObservableBoolean(true)
    val windSpeed = ObservableDouble(0.0)
    val windSpeedExists = ObservableBoolean(true)
    val weatherType = ObservableField<String>("")
    val isLoading = ObservableBoolean(true)

    var icon: MutableLiveData<String> = MutableLiveData()

    private var model: WeatherSimpleModel = WeatherSimpleModel()

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getWeatherData(name: String) {
        uiScope.launch {
            loadData(name)

            weatherType.set(model.type)
            if (model.temperature != null) {
                weatherTemperature.set(model.temperature!! - constKtoC)
                temperatureExists.set(true)
            } else temperatureExists.set(false)
            if (model.humidity != null) {
                humidity.set(model.humidity!!)
                humidityExists.set(true)
            } else humidityExists.set(false)
            if (model.pressure != null){
                pressure.set(model.pressure!!)
                pressureExists.set(true)
            } else pressureExists.set(false)
            if (model.wind != null){
                windSpeed.set(model.wind!!)
                windSpeedExists.set(true)
            } else windSpeedExists.set(false)
            icon.value = model.icon

            isLoading.set(false)
        }
    }

    private suspend fun loadData(name: String) = withContext(Dispatchers.Default) {
        model = interactor.getWeatherData(name)
    }
}