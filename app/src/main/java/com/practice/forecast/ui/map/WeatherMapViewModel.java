package com.practice.forecast.ui.map;

import com.google.android.gms.maps.model.LatLng;
import com.practice.weathermodel.location_api.LocationSupplier;
import com.practice.weathermodel.logger.ILog;
import com.practice.weathermodel.network_api.NetworkClient;
import com.practice.weathermodel.pojo.City;
import com.practice.weathermodel.prefs.Prefs;
import com.practice.weathermodel.utils.Utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class WeatherMapViewModel extends ViewModel implements MapContract.BaseMapViewModel {
    private final static String EMPTY_STRING = "";

    private final MutableLiveData<List<City>> cities = new MutableLiveData<>();
    private final MutableLiveData<LatLng> currentLocation = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final NetworkClient networkClient;
    private final LocationSupplier locationClient;
    private final ILog logger;
    private final Prefs prefs;
    private final Utils utils;

    public WeatherMapViewModel(NetworkClient networkClient, LocationSupplier locationClient, ILog logger, Prefs prefs, Utils utils) {
        this.networkClient = networkClient;
        this.locationClient = locationClient;
        this.logger = logger;
        this.prefs = prefs;
        this.utils = utils;
    }

    @Override
    public LiveData<List<City>> getCitiesWeather() {
        return cities;
    }

    @Override
    public LiveData<LatLng> getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public LiveData<Boolean> getConnectivityState() {
        return isConnected;
    }

    @Override
    public void onConnectivityChange(Observable<Integer> connectivityChangeObservable) {
        compositeDisposable.add(connectivityChangeObservable.subscribe(integer -> isConnected.setValue(utils.isConnectedToNetwork()),
                throwable -> logger.log("WeatherMapViewModel onConnectivityChange() onError: " + throwable.getMessage())));

    }

    @Override
    public void findCurrentLocation(boolean hasPermission) {
        if(!hasPermission){
            return;
        }
        if (prefs.getCoordinates().equals(EMPTY_STRING)) {
                compositeDisposable.add(locationClient.getLastLocationObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(location -> {
                                    logger.log("WeatherMapViewModel findCurrentLocation() onSuccess");
                                    currentLocation.setValue(new LatLng(location.getLatitude(), location.getLongitude()));
                                }, throwable -> logger.log("WeatherMapViewModel findCurrentLocation() onError: " + throwable.getMessage()),
                                () -> logger.log("WeatherMapViewModel findCurrentLocation() onComplete")));
        } else {
            logger.log("WeatherMapViewModel findCurrentLocation() get location from prefs");
            currentLocation.setValue(convertCoordinatesToLatLng(prefs.getCoordinates()));
        }
    }

    private void loadCitiesWeatherWithinRectangle(String coordinates) {
        compositeDisposable.add(networkClient.getCitiesWeatherWithinRectangle(coordinates)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null) {
                        logger.log("ViewModel cities are loaded");

                        cities.setValue(response.getCities());
                    }
                }, throwable -> logger.log("ViewModel cities are NOT loaded error: " + throwable.getMessage())));
    }

    @Override
    public void showCities(BehaviorSubject<String> coordinatesObservable) {
        compositeDisposable.add(coordinatesObservable
                .subscribeOn(Schedulers.io())
                .throttleLast(1, TimeUnit.SECONDS)
                .filter(s -> !s.isEmpty())
                .subscribe(this::loadCitiesWeatherWithinRectangle,
                        throwable -> logger.log("WeatherMapViewModel showCities() onError: " + throwable.getMessage())));
    }

    private LatLng convertCoordinatesToLatLng(String coordinates) {
        final int index = coordinates.lastIndexOf(';');
        double lat = Double.parseDouble(coordinates.substring(0, index));
        double lng = Double.parseDouble(coordinates.substring(index + 1));
        return new LatLng(lat, lng);
    }

    private String convertLatLngToString(LatLng location) {
        return location.latitude + ";" + location.longitude;
    }

    @Override
    public void saveLocation(LatLng location) {
        prefs.putCoordinates(convertLatLngToString(location));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
