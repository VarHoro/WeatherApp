package com.example.weatherapp.presentation.weather

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.weatherapp.constants.IMAGE_URL

import com.example.weatherapp.R
import com.example.weatherapp.constants.constKtoC
import com.example.weatherapp.databinding.ActivityWeatherBinding
import kotlinx.android.synthetic.main.activity_weather.*

class WeatherActivity : AppCompatActivity() {

    var imageUrl = IMAGE_URL

    private lateinit var binding: ActivityWeatherBinding

    private val vm: WeatherViewModel by lazy {
        ViewModelProviders.of(this).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather)
        binding.viewModel = vm

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
                    .into(weather_type_image)
            })


        //weather type
        val weatherType: LiveData<String> = vm.weatherType
        weatherType.observe(this, Observer { type ->
            if (type.isNotEmpty()) {
                weather_type_text.text = type
            } else {
                weather_type_text.visibility = GONE
            }
        })

        //temperature
        val temp: LiveData<Double> = vm.weatherTemperature
        temp.observe(
            this,
            Observer { temper ->
                if (temper != -999.0) {
                    val t = temper - constKtoC //from kelvin to celsius
                    temperature_blank.text =
                        "%.2f".format(t).plus(resources.getString(R.string.temperature_blank)) //°C
                } else {
                    temperature_blank.text = resources.getString(R.string.no_data)
                        .plus(resources.getString(R.string.temperature_blank)) // ---°C
                }
            })

        //humidity
        val hum: LiveData<Double> = vm.humidity
        hum.observe(
            this,
            Observer { humid ->
                if (humid != -1.0) {
                    humidity_text.text =
                        humid.toString().plus(" ").plus(resources.getString(R.string.humidity_blank)) // %
                } else {
                    humidity_text.text = resources.getString(R.string.no_data) // ---
                }
            })

        //pressure
        /*
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
*/
        //wind speed
        val wind: LiveData<Double> = vm.windSpeed
        wind.observe(
            this,
            Observer { windS ->
                if (windS != -1.0) {
                    wind_speed_text.text =
                        windS.toString().plus(" ").plus(resources.getString(R.string.wind_speed_blank)) // m/s
                } else {
                    wind_speed_text.text = resources.getString(R.string.no_data) // ---
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
