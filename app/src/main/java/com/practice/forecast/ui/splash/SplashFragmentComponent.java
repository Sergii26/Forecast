package com.practice.forecast.ui.splash;

import dagger.Component;

@Component(modules = {SplashFragmentModule.class})
public interface SplashFragmentComponent {
    void injectSplashFragment(SplashFragment fragment);
}
