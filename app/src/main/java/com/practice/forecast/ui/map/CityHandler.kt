package com.practice.forecast.ui.map

import com.practice.weathermodel.pojo.City

abstract class CityHandler {
    abstract fun onCityClick(city: City?)
}