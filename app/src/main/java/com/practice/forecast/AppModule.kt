package com.practice.forecast

import com.practice.weathermodel.location_api.LocationClient
import com.practice.weathermodel.location_api.LocationSupplier
import com.practice.weathermodel.network_api.ApiClient
import com.practice.weathermodel.network_api.NetworkClient
import com.practice.weathermodel.prefs.Prefs
import com.practice.weathermodel.prefs.PrefsImpl
import com.practice.weathermodel.receiver.NetworkReceiver
import com.practice.weathermodel.receiver.NetworkStatusChangeReceiver
import com.practice.weathermodel.utils.AndroidUtils
import com.practice.weathermodel.utils.Utils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    fun provideLocationSupplier(): LocationSupplier {
        return LocationClient(App.instance?.applicationContext)
    }

    @Provides
    @Singleton
    fun provideNetworkClient(): NetworkClient {
        return ApiClient()
    }

    @Provides
    fun providePrefs(): Prefs {
        return PrefsImpl(App.instance?.applicationContext);
    }

    @Provides
    fun provideUtils(): Utils {
        return AndroidUtils(App.instance?.applicationContext)
    }

    @Provides
    fun provideNetworkReceiver(): NetworkReceiver {
        return NetworkStatusChangeReceiver()
    }
}