package com.practice.forecast.ui.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailViewModelFactory implements ViewModelProvider.Factory {

    private final ViewModel detailViewModel;

    public DetailViewModelFactory(ViewModel detailViewModel) {
        this.detailViewModel = detailViewModel;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        ViewModel viewModel;
        if(modelClass == DetailViewModel.class){
            viewModel = detailViewModel;
        } else {
            throw new RuntimeException("unsupported view model class: " + modelClass);
        }
        return (T) viewModel;
    }
}
