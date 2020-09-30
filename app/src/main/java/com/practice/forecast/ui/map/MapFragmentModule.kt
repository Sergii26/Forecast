package com.practice.forecast.ui.map

import androidx.lifecycle.ViewModelProvider
import com.practice.forecast.AppComponent
import com.practice.weathermodel.logger.Logger
import dagger.Module
import dagger.Provides

@Module
class MapFragmentModule {
    private val appComponent: AppComponent? = com.practice.forecast.App.instance?.appComponent

    @Provides
    fun provideWeatherViewModelFactory(): ViewModelProvider.Factory {
        return WeatherViewModelFactory(WeatherMapViewModel(appComponent!!.provideNetworkClient(),
                appComponent.provideLocationSupplier(), Logger.withTag("MyLog"),
                appComponent.providePrefs(),
                appComponent.provideUtils(),
                appComponent.provideNetworkReceiver()))
    }

    init {
        appComponent?.injectMapFragment(this)
    }
}