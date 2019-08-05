package com.example.weatherapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.weatherapp.R
import com.example.weatherapp.model.WeatherViewModel

class WeatherActivity : AppCompatActivity() {

    private lateinit var screen: View
    private lateinit var progressBar: ProgressBar
    private val vm: WeatherViewModel by lazy {
        ViewModelProviders.of(this).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        //get cityName from intent, change title and save in vm
        val bundle: Bundle? = intent.extras
        var cn: String = ""
        if (bundle != null) {
            cn = bundle.getString("cityName").toString()
        }
        title = cn

        screen = findViewById(R.id.loading_screen)
        progressBar = findViewById(R.id.weather_progress_bar)

        vm.getWeatherData(cn)

        //weather type
        val weatherType: LiveData<String> = vm.weatherType
        val weatherTypeText = findViewById<TextView>(R.id.weather_type_text)
        weatherType.observe(this, Observer { type -> weatherTypeText.text = type })
        //temperature
        val temp: LiveData<Double> = vm.weatherTemperature
        val tempText = findViewById<TextView>(R.id.temperature_blank)
        temp.observe(this, Observer { temper -> tempText.text = temper.toString() })
        //humidity
        val hum: LiveData<Int> = vm.humidity
        val humText = findViewById<TextView>(R.id.humidity_text)
        hum.observe(this, Observer { humid -> humText.text = humid.toString() })
        //pressure
        val pres: LiveData<Int> = vm.pressure
        val presText = findViewById<TextView>(R.id.pressure_text)
        pres.observe(this, Observer { press -> presText.text = press.toString() })
        //wind speed
        val wind: LiveData<Double> = vm.windSpeed
        val windText = findViewById<TextView>(R.id.wind_speed_text)
        wind.observe(this, Observer { windS -> windText.text = windS.toString() })
        //if name exists then ask for data

        val f: LiveData<Boolean> = vm.isLoading
        f.observe(this, Observer { flag -> showProgressBar(flag) })
    }

    private fun showProgressBar(f: Boolean) {
        if (f) {
            progressBar.visibility = VISIBLE
            screen.visibility = VISIBLE
        } else {
            progressBar.visibility = GONE
            screen.visibility = GONE
        }
    }
}
