package com.practice.forecast.ui.detail;

import com.practice.weathermodel.logger.ILog;
import com.practice.weathermodel.network_api.ApiClient;
import com.practice.weathermodel.network_api.NetworkClient;
import com.practice.weathermodel.pojo.City;
import com.practice.weathermodel.pojo.Main;
import com.practice.weathermodel.pojo.Rain;
import com.practice.weathermodel.pojo.Weather;
import com.practice.weathermodel.pojo.Wind;

import java.util.ArrayList;
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
    private final MutableLiveData<DetailScreenState> stateHolder = new MutableLiveData<>();
    private final ILog logger;

    public MutableLiveData<DetailScreenState> getStateHolderObservable() {
        return stateHolder;
    }

    public DetailViewModel(ILog logger) {
        this.logger = logger;
    }

    public MutableLiveData<List<City>> getCityObservable() {
        return cityObservable;
    }

    public void downloadCity(String cityId){
        logger.log("DetailViewModel downloadCity()");
        stateHolder.setValue(DetailScreenState.createLoadingState());
        compositeDisposable.add(networkClient.getCityWeatherById(cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    final List<City> cities = response.getCities();
                    cities.add(0, getLabelCity());
                    stateHolder.setValue(DetailScreenState.createSetDataState(cities));
                }, throwable -> {
                    stateHolder.setValue(DetailScreenState.createErrorState(throwable));
                    logger.log("DetailViewModel downloadCity() error: " + throwable.getMessage());
                }));
    }

    private City getLabelCity(){
        City city = new City();
        city.setDt(0);
        Main main = new Main();
        Weather weather = new Weather();
        weather.setIcon("label");
        Wind wind = new Wind();
        wind.setSpeed(-1);
        List<Weather> weatherListForCity = new ArrayList<>();
        weatherListForCity.add(0, weather);
        city.setWeather(weatherListForCity);
        city.setWind(wind);
        main.setTemp(-100);
        main.setPressure(-1);
        main.setHumidity(-1);
        Rain rain = new Rain();
        rain.set3h(-1);
        city.setRain(rain);
        city.setMain(main);
        return city;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
