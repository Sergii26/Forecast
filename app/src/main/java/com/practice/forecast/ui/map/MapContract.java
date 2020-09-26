package com.practice.forecast.ui.map;

import com.google.android.gms.maps.model.LatLng;
import com.practice.forecast.ui.arch.Contract;
import com.practice.weathermodel.pojo.City;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.subjects.BehaviorSubject;

public interface MapContract {
    interface BaseMapViewModel {
        LiveData<List<City>> getCitiesWeather();

        LiveData<LatLng> getCurrentLocation();

        LiveData<Boolean> getConnectivityState();

        void onConnectivityChange();

        void saveLocation(LatLng location);

        void showCities(BehaviorSubject<String> coordinatesObservable);

        void findCurrentLocation(boolean hasPermission);
    }

    interface Host extends Contract.Host {
        void showDetailFragment(int cityId, String cityName);
    }
}
