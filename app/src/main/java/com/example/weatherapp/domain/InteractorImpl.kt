package com.example.weatherapp.domain

import com.example.weatherapp.constants.APP_ID
import kotlinx.coroutines.*
import java.io.IOException

import com.example.weatherapp.constants.network_fail
import com.example.weatherapp.constants.server_error
import com.example.weatherapp.data.WeatherDataSourceImpl
import javax.sql.DataSource

class InteractorImpl : Interactor {

    private var model = WeatherSimpleModel()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override suspend fun getWeatherData(name: String): WeatherSimpleModel {

        val dataSource: WeatherDataSource = WeatherDataSourceImpl()
        scope.launch {
            try {
                return dataSource.getData(name)
            } catch (e: IOException) {
                throw Exception(e)
            }

        }

        job.join()
        return model
    }
}