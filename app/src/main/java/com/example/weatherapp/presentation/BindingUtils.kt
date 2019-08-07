package com.example.weatherapp.presentation

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

object BindingUtils {
    @JvmStatic
    @BindingAdapter("goneUnless")
    fun View.goneUnless(visible: Boolean) {
        this.isVisible = visible
    }
}