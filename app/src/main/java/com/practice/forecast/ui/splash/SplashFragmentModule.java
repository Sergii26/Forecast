package com.practice.forecast.ui.splash;

import com.practice.forecast.App;
import com.practice.forecast.AppComponent;
import com.practice.forecast.ui.map.WeatherMapViewModel;
import com.practice.forecast.ui.map.WeatherViewModelFactory;
import com.practice.weathermodel.logger.Logger;

import androidx.lifecycle.ViewModelProvider;
import dagger.Module;
import dagger.Provides;

@Module
public class SplashFragmentModule {

    private final AppComponent appComponent;

    public SplashFragmentModule() {
        this.appComponent = App.getInstance().getAppComponent();
        appComponent.injectSplashFragment(this);
    }

    @Provides
    ViewModelProvider.Factory provideSplashViewModelFactory(){
        return new SplashViewModelFactory(new SplashViewModel(Logger.withTag("MyLog")));
    }
}
