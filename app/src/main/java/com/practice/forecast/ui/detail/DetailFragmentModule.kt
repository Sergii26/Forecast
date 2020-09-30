package com.practice.forecast.ui.detail;

import com.practice.forecast.App;
import com.practice.forecast.AppComponent;
import com.practice.weathermodel.logger.Logger;

import androidx.lifecycle.ViewModelProvider;
import dagger.Module;
import dagger.Provides;

@Module
public class DetailFragmentModule {

    private final AppComponent appComponent;

    public DetailFragmentModule() {
        this.appComponent = App.getInstance().getAppComponent();
        appComponent.injectDetailFragment(this);
    }

    @Provides
    ViewModelProvider.Factory provideDetailViewModelFactory(){
        return new DetailViewModelFactory(new DetailViewModel(Logger.withTag("MyLog")));
    }
}
