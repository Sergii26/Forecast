package com.practice.forecast.ui.detail

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import com.practice.forecast.ui.arch.Contract
import com.practice.weathermodel.pojo.City

interface DetailContract {
    interface BaseDetailViewModel: LifecycleObserver {
        fun downloadCity(cityId: String?)
        fun getCityObservable(): LiveData<List<City>>
        fun getStateHolderObservable(): LiveData<DetailScreenState?>
    }

    interface View : Contract.View {
        fun showProgress()
        fun hideProgress()
        fun setListToAdapter(weatherLIst: List<City?>?)
        fun showError()
    }

    interface Host : Contract.Host {
        fun backToMapFragment()
    }
}