package com.practice.forecast.ui.splash

import dagger.Component

@Component(modules = [SplashFragmentModule::class])
interface SplashFragmentComponent {
    fun injectSplashFragment(fragment: SplashFragment?)
}