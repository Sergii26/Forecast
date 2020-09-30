package com.practice.forecast.ui.detail;

import com.practice.forecast.ui.arch.Contract;
import com.practice.forecast.ui.arch.mvi.ScreenState;
import com.practice.weathermodel.pojo.City;

import java.util.List;

import androidx.lifecycle.LiveData;

public interface DetailContract {

    interface BaseDetailViewModel {

        void downloadCity(String cityId);

        LiveData<List<City>> getCityObservable();
        LiveData<DetailScreenState> getStateHolderObservable();
    }

    interface View extends Contract.View {
        void showProgress();
        void hideProgress();
        void setListToAdapter(List<City> weatherLIst);
        void showError();
    }

    interface Host extends Contract.Host {
        void backToMapFragment();

    }
}
