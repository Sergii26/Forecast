package com.practice.forecast.ui.splash;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SplashViewModelFactory implements ViewModelProvider.Factory {

    private final ViewModel splashViewModel;

    public SplashViewModelFactory(ViewModel splashViewModel) {
        this.splashViewModel = splashViewModel;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        ViewModel viewModel;
        if(modelClass == SplashViewModel.class){
            viewModel = splashViewModel;
        } else {
            throw new RuntimeException("unsupported view model class: " + modelClass);
        }
        return (T) viewModel;
    }
}
