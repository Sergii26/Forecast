package com.practice.forecast.ui.detail

import androidx.lifecycle.ViewModelProvider
import com.practice.forecast.App
import com.practice.forecast.AppComponent
import com.practice.weathermodel.logger.Logger
import dagger.Module
import dagger.Provides


@Module
class DetailFragmentModule {
    private val appComponent: AppComponent? = App.instance?.appComponent

    @Provides
    fun provideDetailViewModelFactory(): ViewModelProvider.Factory {
        return DetailViewModelFactory(DetailViewModel(Logger.withTag("MyLog"), appComponent!!.provideNetworkClient()))
    }

    init {
        appComponent?.injectDetailFragment(this)
    }
}