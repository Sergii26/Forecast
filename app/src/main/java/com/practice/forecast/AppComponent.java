package com.practice.forecast;


import com.practice.forecast.ui.detail.DetailFragmentModule;
import com.practice.forecast.ui.map.MapFragmentModule;
import com.practice.forecast.ui.splash.SplashFragmentModule;
import com.practice.weathermodel.location_api.LocationSupplier;
import com.practice.weathermodel.network_api.NetworkClient;
import com.practice.weathermodel.prefs.Prefs;
import com.practice.weathermodel.utils.Utils;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

   void injectMapFragment(MapFragmentModule module);

   void injectDetailFragment(DetailFragmentModule module);

   void injectSplashFragment(SplashFragmentModule module);

   LocationSupplier provideLocationSupplier();

   NetworkClient provideNetworkClient();

   Prefs providePrefs();

   Utils provideUtils();
}
