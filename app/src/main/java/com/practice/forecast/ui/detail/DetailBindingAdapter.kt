package com.practice.forecast.ui.detail

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.practice.forecast.R
import java.text.SimpleDateFormat
import java.util.*

object DetailBindingAdapter {
    private const val TIME_PATTERN = "HH:mm"
    private const val DAY_PATTERN = "EEEE"
    private val SIMPLE_DATE_FORMAT = SimpleDateFormat()
    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageResource(imageView: ImageView, icon: String?) {
        if (icon != null) {
            when (icon) {
                "01d" -> imageView.setImageResource(R.drawable.icon_01d)
                "02d" -> imageView.setImageResource(R.drawable.icon_02d)
                "03d" -> imageView.setImageResource(R.drawable.icon_03d)
                "04d" -> imageView.setImageResource(R.drawable.icon_04d)
                "09d" -> imageView.setImageResource(R.drawable.icon_09d)
                "10d" -> imageView.setImageResource(R.drawable.icon_10d)
                "11d" -> imageView.setImageResource(R.drawable.icon_11d)
                "13d" -> imageView.setImageResource(R.drawable.icon_13d)
                "50d" -> imageView.setImageResource(R.drawable.icon_50d)
                "01n" -> imageView.setImageResource(R.drawable.icon_01n)
                "02n" -> imageView.setImageResource(R.drawable.icon_02n)
                "03n" -> imageView.setImageResource(R.drawable.icon_03n)
                "04n" -> imageView.setImageResource(R.drawable.icon_04n)
                "09n" -> imageView.setImageResource(R.drawable.icon_09n)
                "10n" -> imageView.setImageResource(R.drawable.icon_10n)
                "11n" -> imageView.setImageResource(R.drawable.icon_11n)
                "13n" -> imageView.setImageResource(R.drawable.icon_13n)
                "50n" -> imageView.setImageResource(R.drawable.icon_50n)
                "label" -> imageView.setImageResource(R.drawable.icon_01d)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("android:text")
    fun setConvertedTime(view: TextView, millis: Int) {
        if (view.id == R.id.tvTime) {
            setTextToView(view, R.string.time_label, TIME_PATTERN, millis)
        }
        if (view.id == R.id.tvDate) {
            setTextToView(view, R.string.date_label, DAY_PATTERN, millis)
        }
    }

    private fun setTextToView(view: TextView, labelResource: Int, timePattern: String, millis: Int) {
        if (millis == 0) {
            view.setText(labelResource)
        } else {
            SIMPLE_DATE_FORMAT.applyPattern(timePattern)
            SIMPLE_DATE_FORMAT.format(Date(millis * 1000L))
            view.text = SIMPLE_DATE_FORMAT.format(Date(millis * 1000L))
        }
    }
}