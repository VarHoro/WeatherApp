package com.example.weatherapp.domain

import android.app.Service
import com.example.weatherapp.constants.APP_ID
import com.example.weatherapp.constants.BASE_URL
import com.example.weatherapp.data.OpenWeatherAPI
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val weatherModule = module{
    single<Retrofit>{Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()}
    single{OpenWeatherAPI::class.java}
    single<Service>{get<Retrofit>().create(get())}


}