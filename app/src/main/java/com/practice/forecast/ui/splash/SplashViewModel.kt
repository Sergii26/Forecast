package com.practice.forecast.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.forecast.ui.splash.SplashContract.BaseSplashViewModel
import com.practice.weathermodel.logger.ILog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashViewModel(private val logger: ILog) : ViewModel(), BaseSplashViewModel {
    private val isReady = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable()

    override fun getIsReady(): LiveData<Boolean> {
        return isReady;
    }

    override fun startTimer() {
        isReady.setValue(false)
        compositeDisposable.add(Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _: Long? ->
                    logger.log("SplashViewModel startTimer() success")
                    isReady.setValue(true) }
                ) { throwable: Throwable -> logger.log("SplashViewModel startTimer() error: " + throwable.message) })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}