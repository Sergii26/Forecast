package com.practice.forecast

import com.practice.forecast.ui.detail.DetailFragmentModule
import com.practice.forecast.ui.map.MapFragmentModule
import com.practice.forecast.ui.splash.SplashFragmentModule
import com.practice.weathermodel.location_api.LocationSupplier
import com.practice.weathermodel.network_api.NetworkClient
import com.practice.weathermodel.prefs.Prefs
import com.practice.weathermodel.receiver.NetworkReceiver
import com.practice.weathermodel.utils.Utils
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectMapFragment(module: MapFragmentModule);
    fun injectDetailFragment(module: DetailFragmentModule);
    fun injectSplashFragment(module: SplashFragmentModule);
    fun provideLocationSupplier(): LocationSupplier
    fun provideNetworkClient(): NetworkClient
    fun providePrefs(): Prefs
    fun provideUtils(): Utils
    fun provideNetworkReceiver(): NetworkReceiver
}