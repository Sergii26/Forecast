package com.practice.forecast.ui.splash

import androidx.lifecycle.ViewModelProvider
import com.practice.forecast.App
import com.practice.forecast.AppComponent
import com.practice.weathermodel.logger.Logger
import dagger.Module
import dagger.Provides

@Module
class SplashFragmentModule {
    private val appComponent: AppComponent? = App.instance?.appComponent

    @Provides
    fun provideSplashViewModelFactory(): ViewModelProvider.Factory {
        return SplashViewModelFactory(SplashViewModel(Logger.withTag("MyLog")))
    }

    init {
        appComponent?.injectSplashFragment(this)
    }
}