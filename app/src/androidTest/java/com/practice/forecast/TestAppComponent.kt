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
@Component(modules = [TestAppModule::class])
interface TestAppComponent: AppComponent {
    override fun injectMapFragment(module: MapFragmentModule);
    override fun injectDetailFragment(module: DetailFragmentModule);
    override fun injectSplashFragment(module: SplashFragmentModule);
    override fun provideLocationSupplier(): LocationSupplier
    override fun provideNetworkClient(): NetworkClient
    override fun providePrefs(): Prefs
    override fun provideUtils(): Utils
    override fun provideNetworkReceiver(): NetworkReceiver
}