package com.example.weatherapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.example.weatherapp.open_weather_api.APP_ID
import com.example.weatherapp.open_weather_api.OpenWeatherAPI
import com.example.weatherapp.open_weather_api.WeatherModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Interactor : IInteractor {
    val BASE_URL = "https://api.openweathermap.org/"
    override fun getWeatherData(name: String): LiveData<WeatherSimpleModel> {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(OpenWeatherAPI::class.java)
        val call = service.getWeatherByCityName(name, APP_ID)
        val model = MutableLiveData<WeatherSimpleModel>()
        call.enqueue(object : Callback<WeatherModel> {
            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                Log.v("Error", t.toString())
            }

            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                val body = response.body()

                if (body != null) {
                    model.value?.humidity = body.main?.humidity
                    model.value?.pressure = body.main?.pressure
                    model.value?.temperature = body.main?.temp
                    model.value?.type = body.weather[0].description
                    model.value?.wind = body.wind?.speed
                    model.value?.icon = body.weather[0].icon
                }
            }
        })
        return model
    }

}