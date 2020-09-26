package com.practice.forecast.ui.detail;

import dagger.Component;

@Component(modules = {DetailFragmentModule.class})
public interface DetailFragmentComponent {
    void injectDetailFragment(DetailFragment fragment);
}
