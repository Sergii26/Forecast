package com.practice.forecast.ui.map

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng
import com.practice.forecast.ui.arch.Contract
import com.practice.weathermodel.pojo.City
import io.reactivex.subjects.BehaviorSubject

interface MapContract {
    interface BaseMapViewModel : LifecycleObserver {
        fun getCitiesWeather(): LiveData<List<City>>
        fun getCurrentLocation(): LiveData<LatLng?>
        fun getConnectivityState(): LiveData<Boolean?>
        fun saveLocation(location: LatLng)
        fun showCities(coordinatesObservable: BehaviorSubject<String>)
        fun findCurrentLocation(hasPermission: Boolean)
    }

    interface Host : Contract.Host {
        fun openDetailFragment(cityName: String?, cityId: Int)
        fun closeApp()
    }
}