package com.example.weatherapp.view

import android.app.AlertDialog
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
    val constKtoC = 273.15

    private lateinit var screen: View
    private lateinit var progressBar: ProgressBar

    private val vm: WeatherViewModel by lazy {
        ViewModelProviders.of(this).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        screen = findViewById(R.id.loading_screen)
        progressBar = findViewById(R.id.weather_progress_bar)
        showProgressBar(true)

        //get cityName from intent, change title
        val bundle: Bundle? = intent.extras
        var cn = ""
        if (bundle != null) {
            cn = bundle.getString("cityName").toString()
        }
        title = cn

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
                    val t = temper - constKtoC //from kelvin to celsius
                    tempText.text = "%.2f".format(t).plus(resources.getString(R.string.temperature_blank)) //°C
                } else {
                    tempText.text = resources.getString(R.string.no_data).plus(resources.getString(R.string.temperature_blank)) // ---°C
                }
            })

        //humidity
        val hum: LiveData<Double> = vm.humidity
        val humText = findViewById<TextView>(R.id.humidity_text)
        hum.observe(
            this,
            Observer { humid ->
                if (humid != -1.0) {
                    humText.text = humid.toString().plus(" ").plus(resources.getString(R.string.humidity_blank)) // %
                } else {
                    humText.text = resources.getString(R.string.no_data) // ---
                }
            })

        //pressure
        val pres: LiveData<Double> = vm.pressure
        val presText = findViewById<TextView>(R.id.pressure_text)
        pres.observe(
            this,
            Observer { press ->
                if (press != 0.0){
                presText.text = press.toString().plus(" ").plus(resources.getString(R.string.pressure_blank)) // hPa
            }else{
                    presText.text = resources.getString(R.string.no_data) // ---
                }})

        //wind speed
        val wind: LiveData<Double> = vm.windSpeed
        val windText = findViewById<TextView>(R.id.wind_speed_text)
        wind.observe(
            this,
            Observer { windS ->
                if (windS != -1.0){
                windText.text = windS.toString().plus(" ").plus(resources.getString(R.string.wind_speed_blank)) // m/s
            } else {
                    windText.text = resources.getString(R.string.no_data) // ---
                }
            })

        //is loading
        val f: LiveData<Boolean> = vm.isLoading
        f.observe(this, Observer { flag -> showProgressBar(flag) })

        //show error
        val er: LiveData<String> = vm.error
        er.observe(this, Observer{ e ->
            if (e.isNotEmpty()){
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.error_title)
                builder.setMessage(e)
                builder.setPositiveButton("OK"){ _, _ ->
                    finish()
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
        })
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
