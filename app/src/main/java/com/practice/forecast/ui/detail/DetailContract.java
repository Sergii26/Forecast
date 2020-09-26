package com.practice.forecast.ui.detail;

import com.practice.weathermodel.pojo.City;

import java.util.List;

import androidx.lifecycle.LiveData;

public interface DetailContract {

    interface BaseDetailViewModel {

        void downloadCity(String cityId);

        LiveData<List<City>> getCityObservable();
    }

    interface Host {

    }
}
