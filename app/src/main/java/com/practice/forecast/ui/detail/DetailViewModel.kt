package com.practice.forecast.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.forecast.ui.detail.DetailContract.BaseDetailViewModel
import com.practice.weathermodel.logger.ILog
import com.practice.weathermodel.network_api.NetworkClient
import com.practice.weathermodel.pojo.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class DetailViewModel(private val logger: ILog, private val networkClient: NetworkClient) : ViewModel(), BaseDetailViewModel {
    private val cityObservable = MutableLiveData<List<City>>()
    private val compositeDisposable = CompositeDisposable()
    private val stateHolderObservable = MutableLiveData<DetailScreenState?>()
    override fun downloadCity(cityId: String?) {
        logger.log("DetailViewModel downloadCity()")
        stateHolderObservable.setValue(DetailScreenState.Companion.createLoadingState())
        compositeDisposable.add(networkClient.getCityWeatherById(cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response ->
                    val cities = response.cities
                    cities.add(0, labelCity)
                    stateHolderObservable.setValue(DetailScreenState.Companion.createSetDataState(cities))
                }) { throwable: Throwable ->
                    stateHolderObservable.setValue(DetailScreenState.Companion.createErrorState(throwable))
                    logger.log("DetailViewModel downloadCity() error: " + throwable.message)
                })
    }

    override fun getCityObservable(): LiveData<List<City>> {
        return cityObservable
    }

    override fun getStateHolderObservable(): LiveData<DetailScreenState?> {
        return stateHolderObservable
    }

    private val labelCity: City
        get() {
            val city = City()
            city.dt = 0
            val main = Main()
            val weather = Weather()
            weather.icon = "label"
            val wind = Wind()
            wind.speed = -1.0
            val weatherListForCity: MutableList<Weather> = ArrayList()
            weatherListForCity.add(0, weather)
            city.weather = weatherListForCity
            city.wind = wind
            main.temp = -100.0
            main.pressure = -1
            main.humidity = -1
            val rain = Rain()
            rain.set3h(-1.0)
            city.rain = rain
            city.main = main
            return city
        }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}