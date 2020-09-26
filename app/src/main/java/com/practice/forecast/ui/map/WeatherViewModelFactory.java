package com.practice.forecast.ui.map;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class WeatherViewModelFactory implements ViewModelProvider.Factory {

    private final ViewModel weatherViewModel;

    public WeatherViewModelFactory(ViewModel weatherViewModel) {
        this.weatherViewModel = weatherViewModel;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        ViewModel viewModel;
        if(modelClass == WeatherMapViewModel.class){
            viewModel = weatherViewModel;
        } else {
            throw new RuntimeException("unsupported view model class: " + modelClass);
        }
        return (T) viewModel;
    }
}
