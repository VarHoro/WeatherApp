package com.example.weatherapp.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import java.io.IOException

class InteractorImpl(private val dataSource: WeatherDataSource) : Interactor {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun getWeatherData(name: String): LiveData<Result<WeatherSimpleModel>> {
        val result = MutableLiveData<Result<WeatherSimpleModel>>()
        var model: WeatherSimpleModel

        scope.launch {
            try {
                model = dataSource.getData(name)
                val resultModel: Result<WeatherSimpleModel> = Result.success(model)
                result.postValue(resultModel)
            } catch (e: IOException) {
                Log.e("InteractorImpl", "Error: ${e.message}")
                val resultError: Result<WeatherSimpleModel> = Result.failure(e)
                result.postValue(resultError)
            }
        }
        return result
    }
}