package com.example.weatherapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide

import com.example.weatherapp.R
import com.example.weatherapp.model.WeatherViewModel

class WeatherActivity : AppCompatActivity() {

    var imageUrl = "https://openweathermap.org/img/wn/%s@2x.png"
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
        var cn = ""
        if (bundle != null) {
            cn = bundle.getString("cityName").toString()
        }
        title = cn

        screen = findViewById(R.id.loading_screen)
        progressBar = findViewById(R.id.weather_progress_bar)

        vm.getWeatherData(cn)

        //icon
        val weatherIcon: LiveData<String> = vm.icon
        val imageView = findViewById<ImageView>(R.id.weather_type_image)
        weatherIcon.observe(
            this,
            Observer { i -> Glide.with(this).load(String.format(imageUrl, i.toString())).into(imageView) })

        //weather type
        val weatherType: LiveData<String> = vm.weatherType
        val weatherTypeText = findViewById<TextView>(R.id.weather_type_text)
        weatherType.observe(this, Observer { type ->
            if (type.isNotEmpty()) {
                weatherTypeText.text = type
            } else {
                weatherTypeText.visibility = GONE
            }
        })

        //temperature
        val temp: LiveData<Double> = vm.weatherTemperature
        val tempText = findViewById<TextView>(R.id.temperature_blank)
        temp.observe(
            this,
            Observer { temper ->
                if (temper != -999.0) {
                    tempText.text = temper.toString().plus(resources.getString(R.string.temperature_blank))
                } else {
                    tempText.visibility = GONE
                }
            })

        //humidity
        val hum: LiveData<Double> = vm.humidity
        val humText = findViewById<TextView>(R.id.humidity_text)
        val hText = findViewById<TextView>(R.id.h_text_view)
        hum.observe(
            this,
            Observer { humid ->
                if (humid != -1.0) {
                    humText.text = humid.toString().plus(" ").plus(resources.getString(R.string.humidity_blank))
                } else {
                    humText.visibility = GONE
                    hText.visibility = GONE
                }
            })

        //pressure
        val pres: LiveData<Double> = vm.pressure
        val presText = findViewById<TextView>(R.id.pressure_text)
        val pText = findViewById<TextView>(R.id.p_text_view)
        pres.observe(
            this,
            Observer { press ->
                if (press != 0.0){
                presText.text = press.toString().plus(" ").plus(resources.getString(R.string.pressure_blank))
            }else{
                    presText.visibility = GONE
                    pText.visibility = GONE
                }})

        //wind speed
        val wind: LiveData<Double> = vm.windSpeed
        val windText = findViewById<TextView>(R.id.wind_speed_text)
        val wText = findViewById<TextView>(R.id.w_text_view)
        wind.observe(
            this,
            Observer { windS ->
                if (windS != -1.0){
                windText.text = windS.toString().plus(" ").plus(resources.getString(R.string.wind_speed_blank))
            } else {
                    windText.visibility = GONE
                    wText.visibility = GONE
                }
            })

        //loading
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
