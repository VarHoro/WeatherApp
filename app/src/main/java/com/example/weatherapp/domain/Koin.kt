package com.example.weatherapp.domain

import com.example.weatherapp.data.OpenWeatherAPI
import com.example.weatherapp.data.WeatherDataSourceImpl
import com.example.weatherapp.presentation.maps.MapViewModel
import com.example.weatherapp.presentation.weather.BASE_URL
import com.example.weatherapp.presentation.weather.WeatherViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.sin

val weatherModule = module {
    viewModel { MapViewModel() }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<OpenWeatherAPI> { get<Retrofit>().create(OpenWeatherAPI::class.java) }
    single<WeatherDataSource> { WeatherDataSourceImpl(get()) }
    single<Interactor> { InteractorImpl(get()) }
    viewModel { WeatherViewModel(get()) }
}