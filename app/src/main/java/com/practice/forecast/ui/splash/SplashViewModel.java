package com.practice.forecast.ui.splash;

import com.practice.weathermodel.logger.ILog;

import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SplashViewModel extends ViewModel implements SplashContract.BaseSplashViewModel {
    private final MutableLiveData<Boolean> isReady = new MutableLiveData<>();
    private final ILog logger;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SplashViewModel(ILog logger) {
        this.logger = logger;
    }

    @Override
    public LiveData<Boolean> getIsReady() {
        return isReady;
    }

    @Override
    public void startTimer(){
        compositeDisposable.add(Observable.timer(1, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(s -> isReady.setValue(true),
                throwable -> logger.log("SplashViewModel startTimer() error" + throwable.getMessage())));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
