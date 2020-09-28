package com.practice.forecast.ui.map;

import com.practice.forecast.App;
import com.practice.forecast.AppComponent;
import com.practice.weathermodel.logger.Logger;

import androidx.lifecycle.ViewModelProvider;
import dagger.Module;
import dagger.Provides;

@Module
public class MapFragmentModule {

    private final AppComponent appComponent;

    public MapFragmentModule() {
        this.appComponent = App.getInstance().getAppComponent();
        appComponent.injectMapFragment(this);
    }

    @Provides
    ViewModelProvider.Factory provideWeatherViewModelFactory(){
        return new WeatherViewModelFactory(new WeatherMapViewModel(appComponent.provideNetworkClient(),
                appComponent.provideLocationSupplier(), Logger.withTag("MyLog"),
                appComponent.providePrefs(),
                appComponent.provideUtils(),
                appComponent.provideNetworkReceiver()));
    }
}
