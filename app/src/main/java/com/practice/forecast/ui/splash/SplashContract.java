package com.practice.forecast.ui.splash;

import com.practice.forecast.ui.arch.Contract;

import androidx.lifecycle.LiveData;

public interface SplashContract {
    interface BaseSplashViewModel{
        void startTimer();
        LiveData<Boolean> getIsReady();
    }

    interface Host extends Contract.Host {
        void openMapFragment();
    }
}
