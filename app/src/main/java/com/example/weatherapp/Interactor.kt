package com.example.weatherapp

import android.util.Log
import com.example.weatherapp.Web.OpenWeatherAPI
import com.example.weatherapp.Web.WeatherModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Interactor :IInteractor{
    val BASE_URL = ""/*TODO: get api from openweathermap*/
    override fun getWeatherData(name: String): WeatherSimpleModel {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(OpenWeatherAPI::class.java)
        val call = service.getWeatherByCityName(name)
        var model:WeatherSimpleModel = WeatherSimpleModel()

        call.enqueue(object : Callback<WeatherModel>{
            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                Log.v("Error", t.toString())
            }

            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                var body = response.body()
                model.type = body?.weatherList?.get(0)?.main.toString()
                model.icon = body?.weatherList?.get(0)?.icon.toString()
                model.temperature = body?.tph?.temp
                model.pressure = body?.tph?.pressure
                model.humidity = body?.tph?.humidity
                model.wind = body?.wind?.speed

                println(model.type) //checking if it worked
            }

        })

        return model
    }

}