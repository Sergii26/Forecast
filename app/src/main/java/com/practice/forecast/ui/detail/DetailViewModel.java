package com.practice.forecast.ui.detail;

import com.practice.weathermodel.logger.ILog;
import com.practice.weathermodel.network_api.ApiClient;
import com.practice.weathermodel.network_api.NetworkClient;
import com.practice.weathermodel.pojo.City;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailViewModel extends ViewModel implements DetailContract.BaseDetailViewModel {

    private final NetworkClient networkClient = new ApiClient();
    private final MutableLiveData<List<City>> cityObservable = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final ILog logger;

    public DetailViewModel(ILog logger) {
        this.logger = logger;
    }

    public MutableLiveData<List<City>> getCityObservable() {
        return cityObservable;
    }

    public void downloadCity(String cityId){
        logger.log("DetailViewModel downloadCity()");
        compositeDisposable.add(networkClient.getCityWeatherById(cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    cityObservable.setValue(response.getCities());
                }, throwable -> logger.log("DetailViewModel downloadCity() error: " + throwable.getMessage())));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
