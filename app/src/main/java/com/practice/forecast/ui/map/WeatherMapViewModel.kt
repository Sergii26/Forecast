package com.practice.forecast.ui.map

import android.location.Location
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.practice.forecast.App
import com.practice.forecast.ui.map.MapContract.BaseMapViewModel
import com.practice.weathermodel.location_api.LocationSupplier
import com.practice.weathermodel.location_api.Result
import com.practice.weathermodel.logger.ILog
import com.practice.weathermodel.network_api.NetworkClient
import com.practice.weathermodel.pojo.City
import com.practice.weathermodel.pojo.Response
import com.practice.weathermodel.prefs.Prefs
import com.practice.weathermodel.receiver.NetworkReceiver
import com.practice.weathermodel.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class WeatherMapViewModel(private val networkClient: NetworkClient, private val locationClient: LocationSupplier, private val logger: ILog,
                          private val prefs: Prefs, private val utils: Utils, private val networkStatusChangeReceiver: NetworkReceiver) : ViewModel(), BaseMapViewModel {
    private val cities = MutableLiveData<List<City>>()
    private val currentLocation = MutableLiveData<LatLng?>()
    private val isConnected = MutableLiveData<Boolean?>()
    private val compositeDisposable = CompositeDisposable()
    private val connectivityDisposable = CompositeDisposable()

    override fun getCurrentLocation(): LiveData<LatLng?> {
        return currentLocation
    }

    override fun getConnectivityState(): LiveData<Boolean?>{
        return isConnected
    }

    override fun getCitiesWeather(): LiveData<List<City>>{
        return cities
    }


    private fun subscribeToConnectivityChange() {
        connectivityDisposable.add(networkStatusChangeReceiver.statusChangeObservable.subscribe({ integer: Int? -> isConnected.setValue(utils.isConnectedToNetwork) }
        ) { throwable: Throwable -> logger.log("WeatherMapViewModel onConnectivityChange() onError: " + throwable.message) })
    }

    override fun findCurrentLocation(hasPermission: Boolean) {
        if (!hasPermission) {
            return
        }
        if (EMPTY_STRING == prefs.coordinates) {
            compositeDisposable.add(locationClient.lastLocationObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result: Result<Location> ->
                        if (result.isFail) {
                            logger.log("WeatherMapViewModel findCurrentLocation() result has error: " + result.error.message)
                        } else {
                            logger.log("WeatherMapViewModel findCurrentLocation() onSuccess")
                            val location = result.data
                            currentLocation.setValue(LatLng(location.latitude, location.longitude))
                        }
                    }, { throwable: Throwable -> logger.log("WeatherMapViewModel findCurrentLocation() onError: " + throwable.message) }
                    ) { logger.log("WeatherMapViewModel findCurrentLocation() onComplete") })
        } else {
            logger.log("WeatherMapViewModel findCurrentLocation() get location from prefs")
            currentLocation.setValue(convertCoordinatesToLatLng(prefs.coordinates))
        }
    }

    private fun loadCitiesWeatherWithinRectangle(coordinates: String) {
        compositeDisposable.add(networkClient.getCitiesWeatherWithinRectangle(coordinates)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response? ->
                    if (response != null) {
                        logger.log("ViewModel cities are loaded")
                        cities.value = response.cities
                    }
                }) { throwable: Throwable -> logger.log("ViewModel cities are NOT loaded error: " + throwable.message) })
    }

    override fun showCities(coordinatesObservable: BehaviorSubject<String>) {
        compositeDisposable.add(coordinatesObservable
                .subscribeOn(Schedulers.io())
                .throttleLast(1, TimeUnit.SECONDS)
                .filter { s: String -> !s.isEmpty() }
                .subscribe({ coordinates: String -> loadCitiesWeatherWithinRectangle(coordinates) }
                ) { throwable: Throwable -> logger.log("WeatherMapViewModel showCities() onError: " + throwable.message) })
    }

    private fun convertCoordinatesToLatLng(coordinates: String): LatLng {
        val index = coordinates.lastIndexOf(';')
        val lat = coordinates.substring(0, index).toDouble()
        val lng = coordinates.substring(index + 1).toDouble()
        return LatLng(lat, lng)
    }

    private fun convertLatLngToString(location: LatLng): String {
        return location.latitude.toString() + ";" + location.longitude
    }

    override fun saveLocation(location: LatLng) {
        prefs.putCoordinates(convertLatLngToString(location))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun registerReceiver() {
        logger.log("ViewModel registerReceiver()")
        if (connectivityDisposable.size() == 0) {
            subscribeToConnectivityChange()
        }
        App.instance?.applicationContext?.registerReceiver(networkStatusChangeReceiver, networkStatusChangeReceiver.intentFilter)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unregisterReceiver() {
        logger.log("ViewModel unregisterReceiver()")
        App.instance?.applicationContext?.unregisterReceiver(networkStatusChangeReceiver)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        connectivityDisposable.clear()
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}