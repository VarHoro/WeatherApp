package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

class WeatherActivity : AppCompatActivity() {

    private val vm: WeatherViewModel by lazy{
        ViewModelProviders.of(this).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        //get cityName from intent, change title and save in vm
        val bundle: Bundle? = intent.extras
        var cn: String = ""
        if (bundle!=null){
            cn = bundle.getString("cityName").toString()
        }
        title = cn

        //if name exists then ask for data
        if (cn != "") {
            vm.getWeatherData(cn)
        }

        //weather type
        val weatherType: LiveData<String> = vm.weatherType
        val weatherTypeText = findViewById<TextView>(R.id.weather_type_text)
        weatherType.observe(this, Observer { type -> weatherTypeText.text = type })
        //temperature
        val temp: LiveData<Double> = vm.weatherTemperature
        val tempText = findViewById<TextView>(R.id.temperature_blank)
        temp.observe(this, Observer { temp -> tempText.text = temp.toString() })
        //humidity
        val hum: LiveData<Int> = vm.humidity
        val humText = findViewById<TextView>(R.id.humidity_text)
        hum.observe(this, Observer { hum -> humText.text = hum.toString() })
        //pressure
        val pres: LiveData<Int> = vm.pressure
        val presText = findViewById<TextView>(R.id.pressure_text)
        pres.observe(this,Observer{press -> presText.text = press.toString()})
        //wind speed
        val wind: LiveData<Double> = vm.windSpeed
        val windText = findViewById<TextView>(R.id.wind_speed_text)
        wind.observe(this, Observer { wind -> windText.text = wind.toString() })

    }
}
