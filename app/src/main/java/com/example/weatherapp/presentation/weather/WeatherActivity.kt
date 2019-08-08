package com.example.weatherapp.presentation.weather

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide

import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityWeatherBinding
import kotlinx.android.synthetic.main.activity_weather.*
import org.koin.android.viewmodel.ext.android.viewModel

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding

    private val weatherViewModel: WeatherViewModel by viewModel()


    companion object {
        fun start(context: Context, cityName: String) {
            context.startActivity(Intent(context, WeatherActivity::class.java).putExtra("cityName", cityName))
        }

        var cityName = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather)
        binding.viewModel = weatherViewModel

        binding.lifecycleOwner = this

        //get cityName from intent, change title
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            cityName = bundle.getString("cityName").toString()
        }
        title = cityName

        weatherViewModel.getWeatherData(cityName)

        //icon
        val weatherIcon: LiveData<String> = weatherViewModel.icon
        weatherIcon.observe(
            this,
            Observer { i ->
                Glide.with(this)
                    .load(i)
                    .into(weather_type_image)
            })

        //show error
        val er: LiveData<String> = weatherViewModel.error
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
}
