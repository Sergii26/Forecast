package com.practice.forecast.ui.splash

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import com.practice.forecast.ui.arch.Contract

interface SplashContract {
    interface BaseSplashViewModel: LifecycleObserver {
        fun startTimer()
        fun getIsReady(): LiveData<Boolean>
    }

    interface Host : Contract.Host {
        fun openMapFragment()
    }
}