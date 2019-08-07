package com.example.weatherapp.domain

import android.util.Log
import kotlinx.coroutines.*
import java.io.IOException

class InteractorImpl(private val dataSource: WeatherDataSource) : Interactor {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override suspend fun getWeatherData(name: String): WeatherSimpleModel {
        var model = WeatherSimpleModel()
        val weatherJob = scope.launch {
            try {
                model = dataSource.getData(name, model)
            } catch (e: IOException) {
                Log.e("InteractorImpl", "Error: ${e.message}")
            }
        }
        weatherJob.join()
        return model
    }
}