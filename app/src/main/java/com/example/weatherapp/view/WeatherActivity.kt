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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.weatherapp.IMAGE_URL

import com.example.weatherapp.R
import com.example.weatherapp.constKtoC
import com.example.weatherapp.databinding.ActivityWeatherBinding
import com.example.weatherapp.model.WeatherViewModel
import org.koin.android.ext.android.bind

class WeatherActivity : AppCompatActivity() {

    var imageUrl = IMAGE_URL

    private lateinit var binding: ActivityWeatherBinding

    private val vm: WeatherViewModel by lazy {
        ViewModelProviders.of(this).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather)

        binding.loadingScreen
        binding.weatherProgressBar
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
        weatherIcon.observe(
            this,
            Observer { i ->
                Glide.with(this)
                    .load(String.format(imageUrl, i.toString()))
                    .into(binding.weatherTypeImage)
            })

        //weather type
        val weatherType: LiveData<String> = vm.weatherType
        weatherType.observe(this, Observer { type ->
            if (type.isNotEmpty()) {
                binding.weatherTypeText.text = type
            } else {
                binding.weatherTypeText.visibility = GONE
            }
        })

        //temperature
        val temp: LiveData<Double> = vm.weatherTemperature
        temp.observe(
            this,
            Observer { temper ->
                if (temper != -999.0) {
                    val t = temper - constKtoC //from kelvin to celsius
                    binding.temperatureBlank.text =
                        "%.2f".format(t).plus(resources.getString(R.string.temperature_blank)) //°C
                } else {
                    binding.temperatureBlank.text = resources.getString(R.string.no_data)
                        .plus(resources.getString(R.string.temperature_blank)) // ---°C
                }
            })

        //humidity
        val hum: LiveData<Double> = vm.humidity
        hum.observe(
            this,
            Observer { humid ->
                if (humid != -1.0) {
                    binding.humidityText.text =
                        humid.toString().plus(" ").plus(resources.getString(R.string.humidity_blank)) // %
                } else {
                    binding.humidityText.text = resources.getString(R.string.no_data) // ---
                }
            })

        //pressure
        val pres: LiveData<Double> = vm.pressure
        pres.observe(
            this,
            Observer { press ->
                if (press != 0.0) {
                    binding.pressureText.text =
                        press.toString().plus(" ").plus(resources.getString(R.string.pressure_blank)) // hPa
                } else {
                    binding.pressureText.text = resources.getString(R.string.no_data) // ---
                }
            })

        //wind speed
        val wind: LiveData<Double> = vm.windSpeed
        wind.observe(
            this,
            Observer { windS ->
                if (windS != -1.0) {
                    binding.windSpeedText.text =
                        windS.toString().plus(" ").plus(resources.getString(R.string.wind_speed_blank)) // m/s
                } else {
                    binding.windSpeedText.text = resources.getString(R.string.no_data) // ---
                }
            })

        //is loading
        val f: LiveData<Boolean> = vm.isLoading
        f.observe(this, Observer { flag -> showProgressBar(flag) })

        //show error
        val er: LiveData<String> = vm.error
        er.observe(this, Observer { e ->
            if (e.isNotEmpty()) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.error_title)
                builder.setMessage(e)
                builder.setPositiveButton("OK") { _, _ ->
                    finish()
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
        })
    }

    private fun showProgressBar(f: Boolean) {
        binding.apply {
            if (f) {
                weatherProgressBar.visibility = VISIBLE
                loadingScreen.visibility = VISIBLE
            } else {
                weatherProgressBar.visibility = GONE
                loadingScreen.visibility = GONE
            }
        }
    }
}
