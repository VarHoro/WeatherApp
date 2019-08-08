package com.example.weatherapp.presentation.weather

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.weatherapp.domain.Interactor
import com.example.weatherapp.domain.WeatherSimpleModel

class WeatherViewModel(private val interactor: Interactor) : ViewModel() {

    companion object Const {
        val constKtoC = 273.15
    }

    val pressure = ObservableDouble(0.0)
    val pressureExists = ObservableBoolean(true)
    val weatherTemperature = ObservableDouble(0.0)
    val temperatureExists = ObservableBoolean(true)
    val humidity = ObservableDouble(0.0)
    val humidityExists = ObservableBoolean(true)
    val windSpeed = ObservableDouble(0.0)
    val windSpeedExists = ObservableBoolean(true)
    val weatherType = ObservableField("")
    val isLoading = ObservableBoolean(true)

    val error = MutableLiveData<String>()
    var icon: MutableLiveData<String> = MutableLiveData()

    private lateinit var liveData: LiveData<Result<WeatherSimpleModel>>
    private lateinit var observer: Observer<Result<WeatherSimpleModel>>

    fun getWeatherData(name: String) {
        liveData = interactor.getWeatherData(name)
        observer = Observer { result ->
            result.fold(
                { value ->
                    updateData(value)
                },
                { exception -> error.value = exception.message }
            )
        }
        liveData.observeForever(observer)
    }

    private fun dataWasReceived() {
        liveData.removeObserver(observer)
    }

    private fun updateData(model: WeatherSimpleModel) {
        weatherType.set(model.type)
        temperatureExists.set(model.temperature != null)
        model.temperature?.let { weatherTemperature.set(it - constKtoC) }
        humidityExists.set(model.humidity != null)
        model.humidity?.let { humidity.set(it) }
        pressureExists.set(model.pressure != null)
        model.pressure?.let { pressure.set(it) }
        windSpeedExists.set(model.wind != null)
        model.wind?.let { windSpeed.set(it) }
        icon.value = model.icon
        isLoading.set(false)
        dataWasReceived()
    }

}
