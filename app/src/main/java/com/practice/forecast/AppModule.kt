package com.practice.forecast;

import com.practice.weathermodel.location_api.LocationClient;
import com.practice.weathermodel.location_api.LocationSupplier;
import com.practice.weathermodel.network_api.ApiClient;
import com.practice.weathermodel.network_api.NetworkClient;
import com.practice.weathermodel.prefs.Prefs;
import com.practice.weathermodel.prefs.PrefsImpl;
import com.practice.weathermodel.receiver.NetworkReceiver;
import com.practice.weathermodel.receiver.NetworkStatusChangeReceiver;
import com.practice.weathermodel.utils.AndroidUtils;
import com.practice.weathermodel.utils.Utils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    public LocationSupplier provideLocationSupplier() {
        return new LocationClient(App.getInstance().getAppContext());
    }

    @Provides
    @Singleton
    public NetworkClient provideNetworkClient() {
        return new ApiClient();
    }

    @Provides
    public Prefs providePrefs() {
        return new PrefsImpl(App.getInstance().getAppContext());
    }

    @Provides
    public Utils provideUtils() {
        return new AndroidUtils(App.getInstance().getAppContext());
    }

    @Provides
    public NetworkReceiver provideNetworkReceiver(){
        return new NetworkStatusChangeReceiver();
    }
}
